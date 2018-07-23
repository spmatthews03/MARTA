package group_a7_8.event;

import edu.gatech.TransitSystem;
import group_a7_8.Path;
import edu.gatech.RailCar;

public class BlockPathEvent extends SimEvent{
	private Path path;
	private RailCar train;
	
	public BlockPathEvent(TransitSystem system, Integer eventID, Integer timeRank, RailCar train, Path path) {
    	super(system,timeRank,"set_path_block",eventID);
		this.train = train;
		this.path = path;
	}

	public Path getPath() {
		return this.path;
	}

	public RailCar getTrain() {
		return this.train;
	}

	@Override
	public void execute() {
		displayEvent();
		//System.out.printf("BlockPathEvent %s:\n\t%s\n", eventType,toJSON());

		this.path.clearIsBlocked();
		this.path.set_delta_stall_duration(0);

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
	    	sb.append(this.path.getPathKey());
	    	return sb.toString();
	}
}
