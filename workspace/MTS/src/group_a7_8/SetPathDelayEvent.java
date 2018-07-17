package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class SetPathDelayEvent extends SimEvent{
	private PathKey pathKey;
	private double delayFactor;
	public SetPathDelayEvent(TransitSystem system, Integer eventID, Integer timeRank, PathKey pathKey, double delayFactor) {
    	super(system,timeRank,"set_path_delay",eventID);
		this.pathKey = pathKey;
		this.delayFactor = delayFactor;
	}
	public PathKey getPathKey() {
		return pathKey;
	}
	public double getDelayFactor() {
		return delayFactor;
	}
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		system.addHazard(pathKey, delayFactor);
		System.out.printf(" %s path delay: %f\n\n",pathKey,system.getPath(pathKey).getDelayFactor());
	}
	
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("setting delay of ");
    	sb.append(delayFactor);
    	sb.append(" for path ");
    	sb.append(pathKey);
    	return sb.toString();
	}
	
}
