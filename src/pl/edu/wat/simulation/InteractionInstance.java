package pl.edu.wat.simulation;

import hla.rti.ReceivedInteraction;

public class InteractionInstance {

    private final double time;

    private final ReceivedInteraction receivedInteraction;

    public InteractionInstance(double time, ReceivedInteraction receivedInteraction) {
        this.time = time;
        this.receivedInteraction = receivedInteraction;
    }

    public double getTime() {
        return time;
    }

    public ReceivedInteraction getReceivedInteraction() {
        return receivedInteraction;
    }
}
