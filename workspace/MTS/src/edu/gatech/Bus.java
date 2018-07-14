package edu.gatech;


import group_a7_8.FuelConsumption;

public class Bus extends Vehicle{
    private double fuelCapacity;
    private double fuelLevel;

    public Bus() {
        this.ID = -1;
        this.vehicleType = "Bus";
    }

    public Bus(TransitSystem system, int uniqueValue) {
        super(system, uniqueValue);
        this.vehicleType = "Bus";
    }

    public Bus(TransitSystem system, int uniqueValue, int inputRoute, int inputLocation, int inputPassengers,
               int inputCapacity, double fuelLevel, double fuelCapacity, int inputSpeed) {
        super(system, uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
        this.fuelCapacity = fuelCapacity;
        this.fuelLevel = fuelLevel;
        this.vehicleType = "Bus";
    }

    public void setFuelCapacity(double inputFuelCapacity) { this.fuelCapacity = inputFuelCapacity; }

    public void setFuelLevel(double inputFuelLevel) { this.fuelLevel = inputFuelLevel; }

    public double getFuelCapacity() { return this.fuelCapacity; }

    public double getFuelLevel() { return this.fuelLevel; }

    public double getFuelConsumed(){ return system.getTotalFuelConsumed(this); }

    public boolean hasEnoughFuel(){
    	//get bus route
    	BusRoute route = system.getBusRoute(getRouteID());
    	
    	//first is get current stop
    	// to get this get the current bust location
    	int currentLocation  = this.getLocation();
    	Stop currentStop = route.getBusStop(system, currentLocation);
    	//next get the next location
    	int nextLocation = route.getNextLocation(currentLocation);
    	Stop nextStop = route.getBusStop(system, nextLocation);
    	double distance = currentStop.findDistance(nextStop);
    	
    	return true;
    }

    public void displayEvent() { this.displayEvent(); }

    public void displayInternalStatus(){ this.displayInternalStatus(); }

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
        sb.append(',');
        sb.append("\"fuelCapacity\":");
        sb.append(this.fuelCapacity);
        sb.append(',');
        sb.append("\"fuelLevel\":");
        sb.append(this.fuelLevel);
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object object){ return this.equals(object); }

}