package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.RailStation;;

public class BlockPathEvent extends SimEvent{
	private RailRoute route;
	private RailStation origin;
	private RailStation destination;
	private PathKey pathKey;
	private Path path;
	private RailCar train;
	
	public BlockPathEvent(TransitSystem system, Integer eventID, Integer timeRank, RailCar train) {
    	super(system,timeRank,"set_path_block",eventID);
		this.route = system.getRailRoute(train.getRouteID());
		
        int currentLocation = train.getLocation();
        int currentStationID = route.getStationID(currentLocation);

        int nextLocation = route.getNextLocation(currentLocation);
        int nextStationID = route.getStationID(nextLocation);

        RailStation currentStation = system.getRailStation(currentStationID);
        RailStation nextStation    = system.getRailStation(nextStationID);
		
		this.origin		 = currentStation;
		this.destination = nextStation;

		this.pathKey = system.getPathKey(origin, destination);
		this.path = system.getPath(pathKey);
		this.train = train;
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

		this.path.clearIsBlocked();

		int time_trigger_resume = this.getRank() + 1;
		VehicleResumeServiceEvent train_repaired_event =
				new VehicleResumeServiceEvent(this.system, this.getEventQueue().getNextEventID(),
											  time_trigger_resume , train);
		
		this.getEventQueue().add(train_repaired_event);
	}
	
	public String getDescription() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append("blocking ");
	    	sb.append(pathKey);
	    	return sb.toString();
	}	
}
