package edu.gatech;


public class Train extends Vehicle {

    public Train() {
        this.ID = -1;
    }

    public Train(int uniqueValue) {
        super(uniqueValue);
    }

    public Train(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
    }



    public void displayEvent() {
        System.out.println(" train: " + Integer.toString(this.ID));
    }

    public void displayInternalStatus() {
        System.out.print("> Train - ID: " + Integer.toString(ID) + " route: " + Integer.toString(route));
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
            Train me = (Train) object;
            if (this.ID == me.getID()) {
                result = true;
            }
        }
        return result;
    }

}
