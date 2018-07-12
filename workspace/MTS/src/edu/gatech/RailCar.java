package edu.gatech;


public class RailCar extends Vehicle {

    public RailCar() {
        this.ID = -1;
    }

    public RailCar(int uniqueValue) {
        super(uniqueValue);
    }

    public RailCar(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
    }

    public void displayEvent() { this.displayEvent("train"); }

    public void displayInternalStatus(){ this.displayInternalStatus("train"); }

    public String toJSON() { return this.toJSON("train"); }

    public boolean equals(Object object){ return this.equals(object); }

}
