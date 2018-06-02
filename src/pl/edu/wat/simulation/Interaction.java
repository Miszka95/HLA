package pl.edu.wat.simulation;

import hla.rti.ArrayIndexOutOfBounds;
import hla.rti.ReceivedInteraction;
import hla.rti.jlc.EncodingHelpers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interaction {

    private double time;

    private InteractionType interactionType;

    private Map<String, Integer> parameters = new HashMap<>();

    public Interaction(double time, InteractionType interactionType) {
        this.time = time;
        this.interactionType = interactionType;
    }

    public void assignParameters(ReceivedInteraction receivedInteraction) {
        List<String> parameterNames = interactionType.getParameters();

        for (int i = 0; i < parameterNames.size(); i++) {
            try {
                this.parameters.put(parameterNames.get(i), EncodingHelpers.decodeInt(receivedInteraction.getValue(i)));
            } catch (ArrayIndexOutOfBounds arrayIndexOutOfBounds) {
                arrayIndexOutOfBounds.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Interaction{" +
                "time=" + time +
                ", interactionType=" + interactionType.getLabel() +
                ", parameters=" + parameters +
                '}';
    }

    public double getTime() {
        return time;
    }

    public InteractionType getInteractionType() {
        return interactionType;
    }

    public Map<String, Integer> getParameters() {
        return parameters;
    }
}
