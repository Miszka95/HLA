package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static pl.edu.wat.simulation.InteractionType.COMPLETE_ORDER;
import static pl.edu.wat.simulation.InteractionType.ORDER_FOOD;

public class KitchenFederate extends Federate {

    private List<Order> orders = new ArrayList<>();

    @Override
    protected void init() {
        ambassador = new KitchenAmbassador();
        ambassador.setFederate(this);
        NAME = "KitchenFederate";
        TIME_STEP = 15.0;
        PUBLISHED_INTERACTIONS = Collections.singletonList(COMPLETE_ORDER);
        SUBSCRIBED_INTERACTIONS = Collections.singletonList(ORDER_FOOD);
    }

    @Override
    protected void run() {
        orders.forEach(Order::cook);
        orders.removeIf(this::readyToServe);
        ambassador.getReceivedInteractions().forEach(i -> createRandomOrder(i.getParameter("id")));
        ambassador.getReceivedInteractions().clear();
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
