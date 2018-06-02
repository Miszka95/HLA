package pl.edu.wat.simulation.statistics;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

import java.util.Arrays;

import static pl.edu.wat.simulation.InteractionType.*;

public class StatisticsFederate extends Federate {

    public static final Double TIME_STEP = 20.0;

    private static final String NAME = "StatisticsFederate";

    @Override
    protected void init() {
        ambassador = new StatisticsAmbassador();
        ambassador.setFederate(this);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, LEAVE_QUEUE, ENTER);
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(TIME_STEP, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new StatisticsFederate().runFederate();
    }
}
