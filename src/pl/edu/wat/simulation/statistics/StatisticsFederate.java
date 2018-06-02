package pl.edu.wat.simulation.statistics;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.InteractionType;

public class StatisticsFederate extends Federate {

    private static final String NAME = "StatisticsFederate";

    @Override
    protected void init() {
        ambassador = new StatisticsAmbassador();
        ambassador.setFederate(this);
        timeStep = 20.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        subscribeInteraction(InteractionType.JOIN_QUEUE);
        subscribeInteraction(InteractionType.LEAVE_QUEUE);
        subscribeInteraction(InteractionType.ENTER);
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
        new StatisticsFederate().runFederate();
    }
}
