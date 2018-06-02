package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.*;

import java.util.*;

import static pl.edu.wat.simulation.InteractionType.COMPLETE_ORDER;
import static pl.edu.wat.simulation.InteractionType.ORDER_FOOD;

public class KitchenFederate extends Federate {

    public static final Double TIME_STEP = 15.0;

    private static final String NAME = "KitchenFederate";

    private List<Order> orders = new ArrayList<>();

    @Override
    protected void init() {
        ambassador = new KitchenAmbassador();
        ambassador.setFederate(this);
        PUBLISHED_INTERACTIONS = Collections.singletonList(COMPLETE_ORDER);
        SUBSCRIBED_INTERACTIONS = Collections.singletonList(ORDER_FOOD);
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(TIME_STEP, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());

            orders.forEach(Order::cook);
            orders.removeIf(this::readyToServe);
            ambassador.getReceivedInteractions().forEach(i -> createRandomOrder(i.getParameters().get("id")));
            ambassador.getReceivedInteractions().clear();

            Federation.tick();
        }
    }

    private void createRandomOrder(int clientId) {
        Order order = Order.create(clientId);
        orders.add(order);
        Logger.log("Preparing meal for client with id %d. Estimated time: %f",
                order.getClientId(), order.getTimeToReady());
    }

    private boolean readyToServe(Order order) {
        if (order.getTimeToReady() >= 0) {
            return false;
        }
        sendInteraction(COMPLETE_ORDER, Collections.singletonList(order.getClientId()));
        return true;
    }

    public static void main(String[] args) {
        new KitchenFederate().runFederate();
    }
}
