package pl.edu.wat.simulation.statistics;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Interaction;
import pl.edu.wat.simulation.InteractionType;
import pl.edu.wat.simulation.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static pl.edu.wat.simulation.InteractionType.*;

public class StatisticsFederate extends Federate {

    private List<Entry> entries = new ArrayList<>();

    private List<Entry> filledEntries = new ArrayList<>();

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
        ambassador.getReceivedInteractions().forEach(this::handleInteraction);
        ambassador.getReceivedInteractions().clear();
        calculateWaitingTime();
    }

    private void handleInteraction(Interaction interaction) {
        if (InteractionType.JOIN_QUEUE.equals(interaction.getInteractionType())) {
            Entry entry = new Entry();
            entry.setClientId(interaction.getParameter("id"));
            entry.setStart(interaction.getTime());
            entries.add(entry);
        } else if (InteractionType.LEAVE_QUEUE.equals(interaction.getInteractionType())) {
            entries.remove(findEntryByClientId(interaction.getParameter("id")));
        } else if (InteractionType.ENTER.equals(interaction.getInteractionType())) {
            Entry entry = findEntryByClientId(interaction.getParameter("id"));
            if (entry != null) {
                entry.setEnd(interaction.getTime());
                entries.remove(entry);
                filledEntries.add(entry);
            }
        }
    }

    private Entry findEntryByClientId(int clientId) {
        return entries.stream()
                .filter(e -> clientId == e.getClientId())
                .findFirst()
                .orElse(null);
    }

    private void calculateWaitingTime() {
        double waitingTime = filledEntries.stream()
                .mapToDouble(e -> e.getEnd() - e.getStart())
                .average()
                .orElse(Double.NaN);

        Logger.log("Average waiting time in queue for %d clients: %f", filledEntries.size(), waitingTime);
    }

    public static void main(String[] args) {
        new StatisticsFederate().runFederate();
    }
}
