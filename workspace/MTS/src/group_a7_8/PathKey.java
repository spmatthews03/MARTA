package group_a7_8;

import java.util.Objects;


import edu.gatech.Facility;

public class PathKey {
	private Facility origin;
	private Facility destination;


	public PathKey(Facility latestStop, Facility beginStop) {
		super();
		this.origin = latestStop;
		this.destination = beginStop;
	}


	public Facility getOrigin() {
		return origin;
	}


	public Facility getDestination() {
		return destination;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(origin.get_uniqueID());
		sb.append('-');
		sb.append(origin.getFacilityName());
		sb.append(" --> ");
		sb.append(destination.get_uniqueID());
		sb.append('-');
		sb.append(destination.getFacilityName());
		return sb.toString();
	}
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"origin\":");
		sb.append(origin.get_uniqueID());
		sb.append(',');
		sb.append("\"destination\":");
		sb.append(destination.get_uniqueID());
		sb.append('}');
		return sb.toString();
	}
	
    //Override the equals method to compare the object
    @Override
    public boolean equals(Object object) {
        boolean result = false;
        if (object == null || object.getClass() != getClass()) {
            result = false;
        } else {
            PathKey me = (PathKey) object;
            if (this.getOrigin().get_uniqueID() == me.getOrigin().get_uniqueID() && this.getDestination().get_uniqueID() == me.getDestination().get_uniqueID()) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
    	int hash = Objects.hash(origin,destination);
    	//System.out.printf("%s hash: %x\n", toString(),hash);
    	return hash;
    }
}
