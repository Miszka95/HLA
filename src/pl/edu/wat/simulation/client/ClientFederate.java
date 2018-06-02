package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.InteractionType;

import java.util.Collections;

public class ClientFederate extends Federate {

    private static final String NAME = "ClientFederate";

    @Override
    protected void init() {
        ambassador = new ClientAmbassador();
        ambassador.setFederate(this);
        timeStep = 2.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(InteractionType.ARRIVE);
        publishInteraction(InteractionType.LEAVE_QUEUE);
        publishInteraction(InteractionType.ENTER);
        publishInteraction(InteractionType.ORDER_FOOD);
        publishInteraction(InteractionType.PAY_AND_LEAVE);
        subscribeInteraction(InteractionType.JOIN_QUEUE);
        subscribeInteraction(InteractionType.ALLOW_TO_ENTER);
        subscribeInteraction(InteractionType.SERVE_ORDER);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(timeStep, ambassador);
            Client client = Client.create();
            sendInteraction(InteractionType.ARRIVE, Collections.singletonList(client.getId()));
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new ClientFederate().runFederate();
    }
}
