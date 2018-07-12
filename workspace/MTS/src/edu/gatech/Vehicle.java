package edu.gatech;

public abstract class Vehicle {
    protected Integer ID;
    protected Integer route;
    protected Integer nextLocation;
    protected Integer prevLocation;
    protected Integer passengers;
    protected Integer capacity;
    protected Integer speed;

    public Vehicle() {
        this.ID = -1;
    }

    public Vehicle(int uniqueValue) {
        this.ID = uniqueValue;
        this.route = -1;
        this.nextLocation = -1;
        this.prevLocation = -1;
        this.passengers = -1;
        this.capacity = -1;
        this.speed = -1;
    }

    public Vehicle(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        this.ID = uniqueValue;
        this.route = inputRoute;
        this.nextLocation = inputLocation;
        this.prevLocation = inputLocation;
        this.passengers = inputPassengers;
        this.capacity = inputCapacity;
        this.speed = inputSpeed;
    }

    public void setRoute(int inputRoute) { this.route = inputRoute; }

    public void setLocation(int inputLocation) {
        this.prevLocation = this.nextLocation;
        this.nextLocation = inputLocation;
    }

    public void setPassengers(int inputPassengers) { this.passengers = inputPassengers; }

    public void setCapacity(int inputCapacity) { this.capacity = inputCapacity; }

    public void setSpeed(int inputSpeed) { this.speed = inputSpeed; }

    public Integer getID() { return this.ID; }

    public Integer getRouteID() { return this.route; }

    public Integer getLocation() { return this.nextLocation; }

    public Integer getPastLocation() { return this.prevLocation; }

    public Integer getPassengers() { return this.passengers; }

    public Integer getCapacity() { return this.capacity; }

    public Integer getSpeed() { return this.speed; }

//    public void displayEvent() {
//        System.out.println(" train: " + Integer.toString(this.ID));
//    }
//
//    public void displayInternalStatus() {
//        System.out.print("> Train - ID: " + Integer.toString(ID) + " route: " + Integer.toString(route));
//        System.out.print(" location from: " + Integer.toString(prevLocation) + " to: " + Integer.toString(nextLocation));
//        System.out.print(" passengers: " + Integer.toString(passengers) + " capacity: " + Integer.toString(capacity));
//        System.out.println(" speed: " + Integer.toString(speed));
//    }
//
//    public String toJSON() {
//        StringBuilder sb = new StringBuilder();
//        sb.append('{');
//        sb.append("\"ID\":");
//        sb.append(this.ID);
//        sb.append(',');
//        sb.append("\"routeID\":");
//        sb.append(this.route);
//        sb.append(',');
//        sb.append("\"capacity\":");
//        sb.append(this.capacity);
//        sb.append(',');
//        sb.append("\"passengers\":");
//        sb.append(this.passengers);
//        sb.append(',');
//        sb.append("\"nextLocation\":");
//        sb.append(this.nextLocation);
//        sb.append(',');
//        sb.append("\"prevLocation\":");
//        sb.append(this.prevLocation);
//        sb.append(',');
//        sb.append("\"speed\":");
//        sb.append(this.speed);
//        sb.append('}');
//        return sb.toString();
//    }

    public void takeTurn() {
        System.out.println("drop off passengers - pickup passengers to capacity - move to next stop");
    }

    public void adjustPassengers(int differential) { passengers = passengers + differential; }

}
