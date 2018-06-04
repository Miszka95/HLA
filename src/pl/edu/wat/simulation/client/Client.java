package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.InteractionType;
import pl.edu.wat.simulation.Logger;
import pl.edu.wat.simulation.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Client {
    private static int COUNTER = 0;

    public static float ARRIVAL_PROBABILITY = 0.5f;
    private static float EAT_AGAIN_PROBABILITY = 0.25f;
    private static float IMPATIENT_PROBABILITY = 0.10f;

    private int id;
    private ClientState state;
    private double timeToFinishEating;

    public enum ClientState {
        OUTSIDE, IN_QUEUE, ORDERED_FOOD, EATING, LEFT
    }

    public static Client create() {
        Client client = new Client();
        client.setId(++COUNTER);
        client.setState(ClientState.OUTSIDE);
        return client;
    }

    public Optional<InteractionType> continueActivity(ClientFederate federate) {
        if (ClientState.EATING.equals(state)) {
            timeToFinishEating -= ClientFederate.TIME_STEP;
            if (timeToFinishEating <= 0) {
                if (isHungry()) {
                    Logger.log("Client with id %d finished eating but is still hungry and ordered new meal", id);
                    state = ClientState.ORDERED_FOOD;
                    return Optional.of(InteractionType.ORDER_FOOD);
                } else {
                    Logger.log("Client with id %d finished eating, paid and left restaurant", id);
                    state = ClientState.LEFT;
                    federate.getRestaurant().freePlace();
                    return Optional.of(InteractionType.PAY_AND_LEAVE);
                }
            }
        } else if (ClientState.IN_QUEUE.equals(state)) {
            if (canEnterFromQueue(federate)) {
                Logger.log("Client with id %d entered restaurant from queue and ordered food", id);
                federate.getRestaurant().takePlace();
                federate.getQueue().remove(this);
                state = ClientState.ORDERED_FOOD;
                return Optional.of(InteractionType.ENTER);
            } else {
                if (isImpatient()) {
                    Logger.log("Client with id %d is impatient and left queue", id);
                    return Optional.of(InteractionType.LEAVE_QUEUE);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<InteractionType> reactToInteraction(Interaction interaction, ClientFederate federate) {
        if (InteractionType.JOIN_QUEUE.equals(interaction.getInteractionType())) {
            int place = interaction.getParameter("place");
            Logger.log("Client with id %d waiting in queue with number %d", id, place);
            Map<Client, Integer> queue = federate.getQueue();
            queue.put(this, place);
            state = ClientState.IN_QUEUE;
            return Optional.empty();
        } else if (InteractionType.ALLOW_TO_ENTER.equals(interaction.getInteractionType())) {
            Logger.log("Client with id %d entered restaurant and ordered food", id);
            federate.getRestaurant().takePlace();
            state = ClientState.ORDERED_FOOD;
            return Optional.of(InteractionType.ENTER);
        } else if (InteractionType.SERVE_ORDER.equals(interaction.getInteractionType())) {
            Logger.log("Client with id %d got his meal from waiter and started eating", id);
            state = ClientState.EATING;
            timeToFinishEating = Randomizer.eatingTime();
            return Optional.empty();
        }
        return Optional.empty();
    }

    private boolean canEnterFromQueue(ClientFederate federate) {
        int freePlaces = federate.getRestaurant().getFreePlaces();
        Map<Client, Integer> queue = federate.getQueue();

        Integer number = queue.get(this);
        ArrayList<Integer> values = new ArrayList<>(queue.values());
        Collections.sort(values);
        int placeInQueue = values.indexOf(number) + 1;
        return placeInQueue <= freePlaces;
    }

    private boolean isHungry() {
        return Randomizer.test(EAT_AGAIN_PROBABILITY);
    }

    private boolean isImpatient() {
        return Randomizer.test(IMPATIENT_PROBABILITY);
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public void setState(ClientState state) {
        this.state = state;
    }

    private Client() {
    }
}
