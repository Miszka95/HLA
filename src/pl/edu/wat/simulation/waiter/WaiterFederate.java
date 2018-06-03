package pl.edu.wat.simulation.waiter;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.Logger;

import java.util.Collections;
import java.util.List;

import static pl.edu.wat.simulation.InteractionType.COMPLETE_ORDER;
import static pl.edu.wat.simulation.InteractionType.SERVE_ORDER;

public class WaiterFederate extends Federate {

    @Override
    protected void init() {
        ambassador = new WaiterAmbassador();
        ambassador.setFederate(this);
        NAME = "WaiterFederate";
        TIME_STEP = 1.0;
        PUBLISHED_INTERACTIONS = Collections.singletonList(SERVE_ORDER);
        SUBSCRIBED_INTERACTIONS = Collections.singletonList(COMPLETE_ORDER);
    }

    @Override
    protected void run() {
        List<Interaction> interactions = ambassador.getReceivedInteractions();
        if (!interactions.isEmpty()) {
            Interaction interaction = interactions.get(0);
            handleInteraction(interaction);
            interactions.remove(interaction);
        }
    }

    private void handleInteraction(Interaction interaction) {
        Integer clientId = interaction.getParameter("id");
        sendInteraction(SERVE_ORDER, Collections.singletonList(clientId));
        Logger.log("Waiter served meal for client %d", clientId);
    }

    public static void main(String[] args) {
        new WaiterFederate().runFederate();
    }
}
