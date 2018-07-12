package edu.gatech;


import group_a7_8.FuelConsumption;

public class Bus extends Vehicle{


    public Bus() {
        this.ID = -1;
    }

    public Bus(int uniqueValue) {
        super(uniqueValue);
    }

    public Bus(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
    }

    public void setFuelCapacity(double inputFuelCapacity) { this.fuelCapacity = inputFuelCapacity; }

    public void setFuelLevel(double inputFuelLevel) { this.fuelLevel = inputFuelLevel; }

    public double getFuelCapacity() { return this.fuelCapacity; }

    public double getFuelLevel() { return this.fuelLevel; }

    public double getFuelConsumed(){ return TransitSystem.getTotalFuelConsumed(this); }

    public void displayEvent() { this.displayEvent("bus"); }

    public void displayInternalStatus(){ this.displayInternalStatus("bus"); }

    public String toJSON() { return this.toJSON("bus"); }

    public boolean equals(Object object){ return this.equals(object); }

}
