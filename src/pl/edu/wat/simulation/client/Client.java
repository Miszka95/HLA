package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.*;

import java.util.Optional;

public class Client {
    private static int COUNTER = 0;

    public static float ARRIVAL_PROBABILITY = 0.5f;
    private static float EAT_AGAIN_PROBABILITY = 0.25f;

    private int id;
    private boolean isImpatient;
    private double timeToLeaveQueue;
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

    public Optional<InteractionType> continueActivity() {
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
                    return Optional.of(InteractionType.PAY_AND_LEAVE);
                }
            }
        }
        //TODO: przypadek w którym klient czeka w kolejce
        return Optional.empty();
    }

    public Optional<InteractionType> reactToInteraction(Interaction interaction) {
        if (InteractionType.ALLOW_TO_ENTER.equals(interaction.getInteractionType())) {
            Logger.log("Client with id %d entered restaurant and ordered food", id);
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

    private boolean isHungry() {
        return Randomizer.test(EAT_AGAIN_PROBABILITY);
    }

    public int getId() {
        return id;
    }

    public boolean isImpatient() {
        return isImpatient;
    }

    public double getTimeToLeaveQueue() {
        return timeToLeaveQueue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImpatient(boolean impatient) {
        isImpatient = impatient;
    }

    public void setTimeToLeaveQueue(double timeToLeaveQueue) {
        this.timeToLeaveQueue = timeToLeaveQueue;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    private Client() {
    }
}
