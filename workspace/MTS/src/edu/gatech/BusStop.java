package edu.gatech;

public class BusStop extends ExchangePoint {

	public BusStop(int uniqueValue, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
			super(uniqueValue, inputName, inputXCoord, inputYCoord,"BusStop");
	        this.waiting = inputRiders;
	}

    public void displayEvent() {
        System.out.println(" bus stop: " + Integer.toString(this.get_uniqueID()));
    }

    public void takeTurn() {
        System.out.println("get new people - exchange with bus when it passes by");
    }

    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
       	sb.append("\"type\":\"busStop\",");
    	sb.append("\"ID\":");
    	sb.append(this.get_uniqueID());
    	sb.append(',');
    	sb.append("\"name\":\"");
    	sb.append(this.getFacilityName());
    	sb.append('\"');
    	sb.append(',');
    	sb.append("\"xCoord\":");
    	sb.append(this.getLocation().getX());
    	sb.append(',');
    	sb.append("\"yCoord\":");
    	sb.append(this.getLocation().getY());
    	sb.append(',');
    	sb.append("\"waiting\":");
    	sb.append(this.get_riders());
    	sb.append(',');
    	sb.append("\"outOfService\":");
    	sb.append((this.get_out_of_service()?"true":"false"));
    	sb.append('}');
    	return sb.toString();
    }
}