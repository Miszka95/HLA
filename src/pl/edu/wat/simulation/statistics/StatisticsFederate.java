package pl.edu.wat.simulation.statistics;

import pl.edu.wat.simulation.Federate;

import java.util.Arrays;

import static pl.edu.wat.simulation.InteractionType.*;

public class StatisticsFederate extends Federate {

    @Override
    protected void init() {
        ambassador = new StatisticsAmbassador();
        ambassador.setFederate(this);
        NAME = "StatisticsFederate";
        TIME_STEP = 20.0;
        SUBSCRIBED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, LEAVE_QUEUE, ENTER);
    }

    @Override
    protected void run() {
    }

    public static void main(String[] args) {
        new StatisticsFederate().runFederate();
    }
}
