package edu.gatech;

public class Depot extends Facility {

	public Depot(int uniqueID, String name, double x, double y) {
		super(uniqueID, name, x, y, "depot");
	}
	
	public Depot() {
		super();
	}

    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
       	sb.append("\"type\":\"Depot\",");
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
    	sb.append('}');
    	return sb.toString();
    }
}