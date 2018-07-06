package group_a7_8;

import edu.gatech.Bus;
import edu.gatech.Stop;

public class PathKey {
	private Stop origin;
	private Stop destination;


	public PathKey(Stop origin, Stop destination) {
		super();
		this.origin = origin;
		this.destination = destination;
	}


	public Stop getOrigin() {
		return origin;
	}


	public Stop getDestination() {
		return destination;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(origin.getID());
		sb.append('-');
		sb.append(origin.getName());
		sb.append(" --> ");
		sb.append(destination.getID());
		sb.append('-');
		sb.append(destination.getName());
		return sb.toString();
	}
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"origin\":");
		sb.append(origin.getID());
		sb.append(',');
		sb.append("\"destination\":");
		sb.append(destination.getID());
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
            if (this.getOrigin().getID() == me.getOrigin().getID() && this.getDestination().getID() == me.getDestination().getID()) {
                result = true;
            }
        }
        return result;
    }

}
