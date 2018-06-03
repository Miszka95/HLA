package pl.edu.wat.simulation;

import hla.rti.SuppliedParameters;
import hla.rti.jlc.EncodingHelpers;

import java.util.*;

public abstract class Federate {

    public static Double TIME_STEP;

    protected static String NAME;

    protected static List<InteractionType> PUBLISHED_INTERACTIONS = Collections.emptyList();
    protected static List<InteractionType> SUBSCRIBED_INTERACTIONS = Collections.emptyList();

    protected Ambassador ambassador;

    private Map<InteractionType, Integer> interactionHandles = new HashMap<>();

    protected final void runFederate() {
        init();
        Federation.join(NAME, ambassador);
        synchronize();
        enableTimePolicy();
        publishAndSubscribe();
        while (ambassador.isRunning()) {
            Federation.advanceTime(TIME_STEP, ambassador);
            Logger.log("%s: %f", NAME, ambassador.getFederateTime());
            run();
            Federation.tick();
        }
    }

    protected abstract void init();

    private void synchronize() {
        Objects.requireNonNull(ambassador);
        Federation.synchronize(ambassador);
    }

    private void enableTimePolicy() {
        Objects.requireNonNull(ambassador);
        Federation.enableTimePolicy(ambassador);
    }

    private void publishAndSubscribe() {
        PUBLISHED_INTERACTIONS.forEach(this::publishInteraction);
        SUBSCRIBED_INTERACTIONS.forEach(this::subscribeInteraction);
    }

    protected abstract void run();

    private void publishInteraction(InteractionType interactionType) {
        interactionHandles.put(interactionType, Federation.publishInteraction(interactionType));
    }

    private void subscribeInteraction(InteractionType interactionType) {
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
}
