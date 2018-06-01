package pl.edu.wat.simulation;

public enum Interaction {
    ARRIVE("InteractionRoot.Arrive"),
    JOIN_QUEUE("InteractionRoot.JoinQueue"),
    ALLOW_TO_ENTER("InteractionRoot.AllowToEnter"),
    LEAVE_QUEUE("InteractionRoot.LeaveQueue"),
    ENTER("InteractionRoot.Enter"),
    ORDER_FOOD("InteractionRoot.OrderFood"),
    COMPLETE_ORDER("InteractionRoot.CompleteOrder"),
    SERVE_ORDER("InteractionRoot.ServeOrder"),
    PAY_AND_LEAVE("InteractionRoot.PayAndLeave");

    private String label;

    Interaction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
