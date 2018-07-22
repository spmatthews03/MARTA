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
		this.train = train;
		this.origin		 = this.train.get_rail_station_current();
		this.destination = this.train.get_rail_station_next();

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
		//System.out.printf("BlockPathEvent %s:\n\t%s\n", eventType,toJSON());

		this.path.clearIsBlocked();

		train.set_prevLocation(0);
		train.set_nextLocation(1);
		int time_trigger_resume = this.getRank() + train.getRepairDuration();
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
