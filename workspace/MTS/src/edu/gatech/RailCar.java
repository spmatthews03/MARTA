package edu.gatech;


public class RailCar extends Vehicle {

    public RailCar() {
        this.ID = -1;
    }

    public RailCar(TransitSystem system, int uniqueValue) {
        super(system, uniqueValue);
    }

    public RailCar(TransitSystem system, int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(system, uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
    }

    public void displayEvent() { this.displayEvent("train"); }

    public void displayInternalStatus(){ this.displayInternalStatus("train"); }

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
        sb.append('}');
        return sb.toString();
    }
    public boolean equals(Object object){ return this.equals(object); }

}
