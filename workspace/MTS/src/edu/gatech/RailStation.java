package edu.gatech;

public class RailStation extends ExchangePoint {

	public RailStation(int uniqueValue, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
		super(uniqueValue, inputName, inputXCoord, inputYCoord,"railStop");
		this.set_riders(inputRiders);
		this.debug_print();

	}

	private void debug_print () {
		String class_name = this.getClass().getName();
		System.out.println(" " + class_name + " Instantiated");
		System.out.println("\t" + this.toJSON());		
	}

    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
       	sb.append("\"type\":\"");
       	sb.append(getType());
       	sb.append("\",");
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
    	sb.append('}');
    	return sb.toString();
    }
}