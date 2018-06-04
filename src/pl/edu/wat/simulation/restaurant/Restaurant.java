package pl.edu.wat.simulation.restaurant;

public class Restaurant {

    private int freePlaces;

    private static final Restaurant instance = new Restaurant();

    public static Restaurant getInstance() {
        return instance;
    }

    public boolean canEnter() {
        return freePlaces > 0;
    }

    public void freePlace() {
        freePlaces += 1;
    }

    public void takePlace() {
        freePlaces -= 1;
    }

    public int getFreePlaces() {
        return freePlaces;
    }

    public void setFreePlaces(int freePlaces) {
        this.freePlaces = freePlaces;
    }

    private Restaurant() {
        this.freePlaces = RestaurantFederate.FREE_PLACES;
    }
}