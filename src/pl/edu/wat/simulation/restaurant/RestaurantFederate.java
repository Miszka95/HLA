package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.Logger;

import java.util.Arrays;
import java.util.Collections;

import static pl.edu.wat.simulation.InteractionType.*;

public class RestaurantFederate extends Federate {

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        ambassador.setFederate(this);
        NAME = "RestaurantFederate";
        TIME_STEP = 15.0;
        PUBLISHED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(ARRIVE, PAY_AND_LEAVE);
    }

    @Override
    protected void run() {
        ambassador.getReceivedInteractions().forEach(this::handleInteraction);
        ambassador.getReceivedInteractions().clear();
    }

    private void handleInteraction(Interaction interaction) {
        if (ARRIVE.equals(interaction.getInteractionType())) {
            handleClientArrival(interaction);
        }
        //TODO: wariant w którym klient czeka w kolejce
    }

    private void handleClientArrival(Interaction interaction) {
        Integer clientId = interaction.getParameter("id");
        sendInteraction(ALLOW_TO_ENTER, Collections.singletonList(clientId));
        Logger.log("Allowing client with id %d to enter", clientId);
    }

    public static void main(String[] args) {
        new RestaurantFederate().runFederate();
    }
}
