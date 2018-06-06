package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static pl.edu.wat.simulation.InteractionType.*;

public class RestaurantFederate extends Federate {

    public static final int FREE_PLACES = 15;

    private static int POSITION_IN_QUEUE = 0;

    private Restaurant restaurant;
    private int restaurantHandle;

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        ambassador.setFederate(this);
        NAME = "RestaurantFederate";
        TIME_STEP = 10.0;
        PUBLISHED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(ARRIVE, ENTER, PAY_AND_LEAVE);
        restaurant = Restaurant.getInstance();
    }

    @Override
    protected void publishAndSubscribe() {
        super.publishAndSubscribe();
        Federation.publishRestaurantObject();
        this.restaurantHandle = Federation.registerRestaurantObject();
    }

    private void updateRestaurantObject(Restaurant restaurant) {
        Federation.updateRestaurantObject(restaurantHandle, restaurant, ambassador);
    }

    @Override
    protected void run() {
        Logger.log("Free places in restaurant: %d", restaurant.getFreePlaces());
        List<Interaction> interactions = ambassador.getReceivedInteractions();
        Interaction.filter(interactions, PAY_AND_LEAVE).forEach(this::handleInteraction);
        Interaction.filter(interactions, ENTER).forEach(this::handleInteraction);
        restaurant.setTempPlaces(restaurant.getFreePlaces());
        Interaction.filter(interactions, ARRIVE).forEach(this::handleInteraction);
        interactions.clear();
        updateRestaurantObject(restaurant);
        Logger.log("Free places in restaurant: %d", restaurant.getFreePlaces());
    }

    private void handleInteraction(Interaction interaction) {
        if (PAY_AND_LEAVE.equals(interaction.getInteractionType())) {
            restaurant.freePlace();
            Logger.log("Client with id %d left restaurant", interaction.getParameter("id"));
        } else if (ENTER.equals(interaction.getInteractionType())) {
            restaurant.takePlace();
            Logger.log("Client with id %d entered restaurant", interaction.getParameter("id"));
        } else if (ARRIVE.equals(interaction.getInteractionType())) {
            handleClientArrival(interaction);
        }
    }

    private void handleClientArrival(Interaction interaction) {
        Integer clientId = interaction.getParameter("id");
        if (restaurant.canEnter()) {
            restaurant.takeTempPlace();
            sendInteraction(ALLOW_TO_ENTER, Collections.singletonList(clientId));
            Logger.log("Client with id %d allowed to enter without queue", clientId);
        } else {
            int place = ++POSITION_IN_QUEUE;
            sendInteraction(JOIN_QUEUE, Arrays.asList(clientId, place));
            Logger.log("Client with id %d placed in queue with number %d", clientId, place);
        }
    }

    public static void main(String[] args) {
        new RestaurantFederate().runFederate();
    }
}
