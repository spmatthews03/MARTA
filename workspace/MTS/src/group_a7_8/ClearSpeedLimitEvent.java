package group_a7_8;

import java.util.ArrayList;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class ClearSpeedLimitEvent extends SimEvent{
	private PathKey pathKey;
	public ClearSpeedLimitEvent(TransitSystem system, Integer eventID, Integer timeRank, PathKey pathKey) {
    	super(system,timeRank,"clear_speed_limit",eventID);
		this.pathKey = pathKey;
	}
	public PathKey getPathKey() {
		return pathKey;
	}
	
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		system.clearSpeedLimit(pathKey);
		System.out.printf(" %s speed limit: %f\n\n",pathKey,system.getPath(pathKey).getSpeedLimit());
	}
	
	
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("removing speed limit for ");
    	sb.append(pathKey);
    	return sb.toString();
	}		
}