package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.InteractionType;
import pl.edu.wat.simulation.Randomizer;

import java.util.*;

import static pl.edu.wat.simulation.InteractionType.*;

public class ClientFederate extends Federate {

    private List<Client> clients = new ArrayList<>();

    @Override
    protected void init() {
        ambassador = new ClientAmbassador();
        ambassador.setFederate(this);
        NAME = "ClientFederate";
        TIME_STEP = 2.0;
        PUBLISHED_INTERACTIONS = Arrays.asList(ARRIVE, LEAVE_QUEUE, ENTER, ORDER_FOOD, PAY_AND_LEAVE);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER, SERVE_ORDER);
    }

    @Override
    protected void run() {
        for (Client client : clients) {
            Optional<Interaction> interaction = findInteractionForClient(client);

            Optional<InteractionType> reaction = interaction.isPresent()
                    ? client.reactToInteraction(interaction.get())
                    : client.continueActivity();

            reaction.ifPresent(r -> handleClientReaction(client, r));
        }
        ambassador.getReceivedInteractions().clear();
        spawnClientWithProbability();
    }

    private void handleClientReaction(Client client, InteractionType reaction) {
        if (PAY_AND_LEAVE.equals(reaction)) {
            sendInteraction(PAY_AND_LEAVE, Collections.emptyList());
        } else if (ENTER.equals(reaction)) {
            sendInteraction(ENTER, Collections.singletonList(client.getId()));
            sendInteraction(ORDER_FOOD, Collections.singletonList(client.getId()));
        }
        else {
            sendInteraction(reaction, Collections.singletonList(client.getId()));
        }
    }

    private Optional<Interaction> findInteractionForClient(Client client) {
        return ambassador.getReceivedInteractions().stream()
                .filter(i -> i.getParameter("id") == client.getId())
                .findFirst();
    }

    private void spawnClientWithProbability() {
        if (Randomizer.test(Client.ARRIVAL_PROBABILITY)) {
            Client client = Client.create();
            clients.add(client);
            sendInteraction(ARRIVE, Collections.singletonList(client.getId()));
        }
    }

    public static void main(String[] args) {
        new ClientFederate().runFederate();
    }
}
