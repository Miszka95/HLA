package pl.edu.wat.simulation.client;

import java.util.Random;

public class Client {

    private static int COUNTER = 0;
    private static float IMPATIENT_PROBABILITY = 0.5f;
    private static final Random random = new Random();

    private int id;
    private boolean isImpatient;
    private double timeToLeaveQueue;

    public static Client create() {
        Client client = new Client();
        client.setId(++COUNTER);
        client.setImpatient(random.nextFloat() < IMPATIENT_PROBABILITY);
        if (client.isImpatient) {
            client.setTimeToLeaveQueue(10000);
        }
        return client;
    }

    public int getId() {
        return id;
    }

    public boolean isImpatient() {
        return isImpatient;
    }

    public double getTimeToLeaveQueue() {
        return timeToLeaveQueue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImpatient(boolean impatient) {
        isImpatient = impatient;
    }

    public void setTimeToLeaveQueue(double timeToLeaveQueue) {
        this.timeToLeaveQueue = timeToLeaveQueue;
    }

    private Client() {
    }
}
