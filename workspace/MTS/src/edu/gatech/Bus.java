package edu.gatech;



public class Bus extends Vehicle{
    private double fuelCapacity;
    private double fuelLevel;
    private TransitSystem system;
    private boolean refueling;

    public Bus() {
        this.ID = -1;
        this.vehicleType = "Bus";
        this.system = system;
    }

    public Bus(TransitSystem system, int uniqueValue) {
        super(uniqueValue);
        this.vehicleType = "Bus";
        this.system = system;
        this.refueling = false;
    }

    public Bus(TransitSystem system, int uniqueValue, int inputRoute, int inputLocation, int inputPassengers,
               int inputCapacity, double fuelLevel, double fuelCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
        this.fuelCapacity = fuelCapacity;
        this.fuelLevel = fuelLevel;
        this.vehicleType = "Bus";
        this.system = system;
        this.refueling = false;
    }

    public Bus(TransitSystem system, int uniqueValue, int routeId) {
        super(uniqueValue);
        this.vehicleType = "Bus";
        this.route = routeId;
        this.system = system;
        this.refueling = false;
	}

	public void setRefueling(boolean refueling){ this.refueling = refueling; }

	public void setFuelCapacity(double inputFuelCapacity) { this.fuelCapacity = inputFuelCapacity; }

    public void setFuelLevel(double inputFuelLevel) { this.fuelLevel = inputFuelLevel; }

    public boolean isRefueling(){ return this.refueling; }

    public double getFuelCapacity() { return this.fuelCapacity; }

    public double getFuelLevel() { return this.fuelLevel; }

    public double getFuelConsumed(){ return system.getTotalFuelConsumed(this); }

    public void refuel(){ this.fuelLevel = fuelCapacity; }

    public boolean hasEnoughFuel(double distance){
        if(fuelLevel > distance) {
            return true;
        }
        else {
            return false;
        }
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"type\":\"");
        sb.append(this.vehicleType);
        sb.append("\",");
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
        sb.append(',');
        sb.append("\"outOfService\":");
        sb.append((this.getOutOfService()?"true":"false"));
        sb.append(',');
        sb.append("\"refueling\":");
        sb.append(this.refueling);
        sb.append('}');
        return sb.toString();
    }

    
    //this is an invalid equals implementation, causes stack overflow
//    public boolean equals(Object object){ 
//    	return this.equals(object); }

}