package pl.edu.wat.simulation;

import hla.rti.RTIambassador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class Federate {

    protected RTIambassador rti = Federation.getRti();
    protected Ambassador ambassador;
    protected Map<Interaction, Integer> interactionHandles = new HashMap<>();
    protected double timeStep;

    protected final void runFederate() {
        init();
        synchronize();
        enableTimePolicy();
        publishAndSubscribe();
        run();
    }

    protected abstract void init();

    protected void synchronize() {
        Objects.requireNonNull(ambassador);
        Federation.synchronize(ambassador);
    }

    protected void enableTimePolicy() {
        Objects.requireNonNull(ambassador);
        Federation.enableTimePolicy(ambassador);
    }

    protected abstract void publishAndSubscribe();

    protected abstract void run();

    protected void publishInteraction(Interaction interaction) {
        interactionHandles.put(interaction, Federation.publishInteraction(interaction));
    }

    protected void subscribeInteraction(Interaction interaction) {
        interactionHandles.put(interaction, Federation.subscribeInteraction(interaction));
    }
}
