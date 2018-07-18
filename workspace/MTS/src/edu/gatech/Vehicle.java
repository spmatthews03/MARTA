package edu.gatech;

public abstract class Vehicle {
    protected Integer ID;
    protected Integer route;
    protected Integer nextLocation;
    protected Integer prevLocation;
    protected Integer passengers;
    protected Integer capacity;
    protected Integer speed;
    protected TransitSystem system;
    protected String vehicleType;
    protected Boolean outOfService;
    protected Integer delta_stall_duration;

    public Vehicle() {
        this.ID = -1;
        outOfService = false;
    }

    public Vehicle(int uniqueValue) {
        this.ID = uniqueValue;
        this.route = -1;
        this.nextLocation = -1;
        this.prevLocation = -1;
        this.passengers = -1;
        this.capacity = -1;
        this.speed = -1;
        outOfService = false;
    }

    public Vehicle(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        this.ID = uniqueValue;
        this.route = inputRoute;
        this.nextLocation = inputLocation;
        this.prevLocation = inputLocation;
        this.passengers = inputPassengers;
        this.capacity = inputCapacity;
        this.speed = inputSpeed;
        outOfService = false;
    }

    public void setRoute(int inputRoute) { this.route = inputRoute; }

    public void setLocation(int inputLocation) {
        this.prevLocation = this.nextLocation;
        this.nextLocation = inputLocation;
    }

    public void set_prevLocation(int inputLocation) {
        this.prevLocation = inputLocation;
    }

    public void set_nextLocation(int inputLocation) {
        this.nextLocation = inputLocation;
    }

    public void setPassengers(int inputPassengers) { this.passengers = inputPassengers; }

    public void setCapacity(int inputCapacity) { this.capacity = inputCapacity; }

    public void setSpeed(int inputSpeed) { this.speed = inputSpeed; }

    public void setOutOfService(Boolean outOfService){ this.outOfService = outOfService; }

    public boolean getOutOfService(){
        return this.outOfService;
    }
    
    public void set_delta_stall_duration(int stall_duration) {
    	this.delta_stall_duration = stall_duration;
	}

    public Integer getID() { return this.ID; }

    public Integer getRouteID() { return this.route; }

    public Integer getLocation() { return this.nextLocation; }

    public Integer getPastLocation() { return this.prevLocation; }

    public Integer getPassengers() { return this.passengers; }

    public Integer getCapacity() { return this.capacity; }

    public Integer getSpeed() { return this.speed; }
    
    public Integer get_delta_stall_duration() {
    	return this.delta_stall_duration;
	}

    public String getType() {
		return vehicleType;
	}

	public void displayEvent() {
        System.out.println(this.vehicleType + ": " + Integer.toString(this.ID));
    }

    public void displayInternalStatus() {
        System.out.print("> " + this.vehicleType + " - ID: " + Integer.toString(ID) + " route: " + Integer.toString(route));
        System.out.print(" location from: " + Integer.toString(prevLocation) + " to: " + Integer.toString(nextLocation));
        System.out.print(" passengers: " + Integer.toString(passengers) + " capacity: " + Integer.toString(capacity));
        System.out.println(" speed: " + Integer.toString(speed));
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
            Vehicle me = (Vehicle) object;
            if (this.ID == me.getID()) {
                result = true;
            }
        }
        return result;
    }

    abstract public String toJSON();
}
