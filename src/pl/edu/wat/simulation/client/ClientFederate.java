package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.Interaction;

public class ClientFederate extends Federate {

    private static final String NAME = "ClientFederate";

    @Override
    protected void init() {
        ambassador = new ClientAmbassador();
        timeStep = 2.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(Interaction.ARRIVE);
        publishInteraction(Interaction.LEAVE_QUEUE);
        publishInteraction(Interaction.ENTER);
        publishInteraction(Interaction.ORDER_FOOD);
        publishInteraction(Interaction.PAY_AND_LEAVE);
        subscribeInteraction(Interaction.JOIN_QUEUE);
        subscribeInteraction(Interaction.ALLOW_TO_ENTER);
        subscribeInteraction(Interaction.SERVE_ORDER);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(timeStep, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new ClientFederate().runFederate();
    }
}
