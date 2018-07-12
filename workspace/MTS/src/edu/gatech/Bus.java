package edu.gatech;


import group_a7_8.FuelConsumption;

public class Bus extends Vehicle{
    private double fuelCapacity;
    private double fuelLevel;

    public Bus() {
        this.ID = -1;
    }

    public Bus(int uniqueValue) {
        this.ID = uniqueValue;
        this.route = -1;
        this.nextLocation = -1;
        this.prevLocation = -1;
        this.passengers = -1;
        this.capacity = -1;
        this.speed = -1;
    }

    public Bus(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        this.ID = uniqueValue;
        this.route = inputRoute;
        this.nextLocation = inputLocation;
        this.prevLocation = inputLocation;
        this.passengers = inputPassengers;
        this.capacity = inputCapacity;
        this.speed = inputSpeed;
   }


    public void setFuelCapacity(double inputFuelCapacity) { this.fuelCapacity = inputFuelCapacity; }

    public void setFuelLevel(double inputFuelLevel) { this.fuelLevel = inputFuelLevel; }


    public double getFuelCapacity() { return this.fuelCapacity; }

    public double getFuelLevel() { return this.fuelLevel; }

    //public double getFuelConsumed(){ return }

    public void displayEvent() {
        System.out.println(" bus: " + Integer.toString(this.ID));
    }

    public void displayInternalStatus() {
        System.out.print("> bus - ID: " + Integer.toString(ID) + " route: " + Integer.toString(route));
        System.out.print(" location from: " + Integer.toString(prevLocation) + " to: " + Integer.toString(nextLocation));
        System.out.print(" passengers: " + Integer.toString(passengers) + " capacity: " + Integer.toString(capacity));
        System.out.println(" speed: " + Integer.toString(speed));
    }

    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"ID\":");
    	sb.append(this.ID);
    	sb.append(',');
    	sb.append("\"routeID\":");
    	sb.append(this.route);
    	sb.append(',');
    	sb.append("\"capacity\":");
    	sb.append(this.capacity);
    	sb.append(',');
    	sb.append("\"passengers\":");
    	sb.append(this.passengers);
    	sb.append(',');
    	sb.append("\"nextLocation\":");
    	sb.append(this.nextLocation);
    	sb.append(',');
    	sb.append("\"prevLocation\":");
    	sb.append(this.prevLocation);
    	sb.append(',');
    	sb.append("\"speed\":");
    	sb.append(this.speed);
    	sb.append("\"fuelCapacity\":");
    	sb.append(this.fuelCapacity);
    	sb.append("\"fuelLevel\":");
    	sb.append(this.fuelLevel);
    	sb.append('}');
    	return sb.toString();
    }

    public void takeTurn() {
        System.out.println("drop off passengers - pickup passengers to capacity - move to next stop");
    }

    public void adjustPassengers(int differential) { passengers = passengers + differential; }

    //Override the equals method to compare the object
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            Bus me = (Bus) object;
            if (this.ID == me.getID()) {
                result = true;
            }
        }
        return result;
    }

}
