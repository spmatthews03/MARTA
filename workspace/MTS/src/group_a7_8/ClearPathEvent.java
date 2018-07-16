package group_a7_8;

import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.RailStation;
import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class ClearPathEvent extends SimEvent{
	private RailRoute route;
	private RailStation origin;
	private RailStation destination;
	private PathKey pathKey;
	private Path path;
	
	public ClearPathEvent(TransitSystem system, Integer eventID, Integer timeRank, RailCar train) {
    	super(system,timeRank,"clear_path_block",eventID);
		this.route = system.getRailRoute(train.getRouteID());
		this.origin = system.getRailStation(this.route.getStationID(train.getPastLocation()));
		this.destination = system.getRailStation(this.route.getStationID(train.getLocation()));
		this.pathKey = system.getPathKey(origin, destination);
		this.path = system.getPath(pathKey);
	}

	public RailRoute getRoute() {
		return route;
	}
	
	public RailStation getOrigin() {
		return origin;
	}
	
	public RailStation getDestination() {
		return destination;
	}
	
	public PathKey getPathKey() {
		return pathKey;
	}
	
	public Path getPath() {
		return path;
	}
	
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		
		path.clearIsBlocked();;
		System.out.printf(" %s path is blocked\n\n",pathKey);
	}
	
	public String toJSON() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append('{');
	    	sb.append("\"ID\":");
	    	sb.append(eventID);
	    	sb.append(",\"time\":");
	    	sb.append(timeRank);
	    	sb.append(",\"type\":\"");
	    	sb.append(eventType);
	    	sb.append("\",\"pathKey\":");
	    	sb.append(pathKey);
	    	sb.append(",\"blockStatus\":");
	    	sb.append(false);
	    	sb.append('}');
	    	return sb.toString();
	}	
}

