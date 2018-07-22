package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class SetSpeedLimitEvent extends SimEvent{
	private PathKey pathKey;
	private Integer speedLimit;
	public SetSpeedLimitEvent(TransitSystem system, Integer eventID, Integer timeRank, PathKey pathKey, Integer speedLimit) {
    	super(system,timeRank,"set_speed_limit",eventID);
		this.pathKey = pathKey;
		this.speedLimit = speedLimit;
	}
	public PathKey getPathKey() {
		return pathKey;
	}
	public double getSpeedLimit() {
		return speedLimit;
	}
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		this.system.setSpeedLimit(pathKey, speedLimit);
		System.out.printf(" %s speed limit: %d\n\n",pathKey,system.getPath(pathKey).getSpeedLimit());
	}
	
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("setting speed limit for ");
    	sb.append(pathKey);
    	sb.append(" to ");
    	sb.append(speedLimit);
    	return sb.toString();
	}	
}
