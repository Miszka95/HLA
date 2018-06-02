package pl.edu.wat.simulation;

import hla.rti.RTIambassador;
import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class Federate {

    protected RTIambassador rti = Federation.getRti();
    protected Ambassador ambassador;
    protected Map<InteractionType, Integer> interactionHandles = new HashMap<>();
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

    protected void publishInteraction(InteractionType interactionType) {
        interactionHandles.put(interactionType, Federation.publishInteraction(interactionType));
    }

    protected void subscribeInteraction(InteractionType interactionType) {
        interactionHandles.put(interactionType, Federation.subscribeInteraction(interactionType));
    }

    protected void sendInteraction(InteractionType interactionType, List<Integer> parameters) {
        Integer interactionHandle = interactionHandles.get(interactionType);
        SuppliedParameters suppliedParameters = Federation.getSuppliedParameters();

        if (!parameters.isEmpty() && parameters.size() == interactionType.getParameters().size()) {
            int size = parameters.size();
            List<String> parameterNames = interactionType.getParameters();
            for (int i = 0; i < size; i++) {
                suppliedParameters.add(
                        Federation.getParameterHandle(parameterNames.get(i), interactionHandle),
                        EncodingHelpers.encodeInt(parameters.get(i)));
            }
        }
        Federation.sendInteraction(interactionHandle, suppliedParameters, ambassador);
    }

    public Map<InteractionType, Integer> getInteractionHandles() {
        return interactionHandles;
    }

    public void setInteractionHandles(Map<InteractionType, Integer> interactionHandles) {
        this.interactionHandles = interactionHandles;
    }
}
