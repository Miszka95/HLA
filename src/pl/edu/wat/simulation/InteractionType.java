package pl.edu.wat.simulation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum InteractionType {
    ARRIVE("InteractionRoot.Arrive", Collections.singletonList("id")),
    JOIN_QUEUE("InteractionRoot.JoinQueue", Arrays.asList("id", "place")),
    ALLOW_TO_ENTER("InteractionRoot.AllowToEnter", Collections.singletonList("id")),
    LEAVE_QUEUE("InteractionRoot.LeaveQueue", Collections.singletonList("id")),
    ENTER("InteractionRoot.Enter", Collections.singletonList("id")),
    ORDER_FOOD("InteractionRoot.OrderFood", Collections.singletonList("id")),
    COMPLETE_ORDER("InteractionRoot.CompleteOrder", Collections.singletonList("id")),
    SERVE_ORDER("InteractionRoot.ServeOrder", Collections.singletonList("id")),
    PAY_AND_LEAVE("InteractionRoot.PayAndLeave", Collections.emptyList());

    private String label;
    private List<String> parameters;

    InteractionType(String label, List<String> parameters) {
        this.label = label;
        this.parameters = parameters;
    }

    public String getLabel() {
        return label;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
