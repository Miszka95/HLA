package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.Randomizer;

public class Order {

    private int clientId;

    private double timeToReady;

    public static Order create(int clientId) {
        Order order = new Order();
        order.setClientId(clientId);
        order.setTimeToReady(Randomizer.preparationTime());
        return order;
    }

    public void cook() {
        timeToReady -= KitchenFederate.TIME_STEP;
    }

    private Order() {
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public double getTimeToReady() {
        return timeToReady;
    }

    public void setTimeToReady(double timeToReady) {
        this.timeToReady = timeToReady;
    }
}
