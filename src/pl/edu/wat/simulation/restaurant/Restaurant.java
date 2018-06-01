package pl.edu.wat.simulation.restaurant;

public class Restaurant {
    private int freePlaces;
    private int queueSize;

    public boolean canEnter() {
        return freePlaces > 0;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
}