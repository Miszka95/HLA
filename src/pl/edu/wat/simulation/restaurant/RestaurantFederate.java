package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.*;

import java.util.Arrays;
import java.util.Collections;

import static pl.edu.wat.simulation.InteractionType.*;

public class RestaurantFederate extends Federate {

    public static final Double TIME_STEP = 15.0;

    private static final String NAME = "RestaurantFederate";

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        ambassador.setFederate(this);
        PUBLISHED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(ARRIVE, PAY_AND_LEAVE);
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(TIME_STEP, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());

            ambassador.getReceivedInteractions().forEach(this::handleInteraction);
            ambassador.getReceivedInteractions().clear();

            Federation.tick();
        }
    }

    private void handleInteraction(Interaction interaction) {
        if (ARRIVE.equals(interaction.getInteractionType())) {
            handleClientArrival(interaction);
        }
        //TODO: wariant w którym klient czeka w kolejce
    }

    private void handleClientArrival(Interaction interaction) {
        Integer clientId = interaction.getParameters().get("id");
        sendInteraction(ALLOW_TO_ENTER, Collections.singletonList(clientId));
        Logger.log("Allowing client with id %d to enter", clientId);
    }

    public static void main(String[] args) {
        new RestaurantFederate().runFederate();
    }
}
