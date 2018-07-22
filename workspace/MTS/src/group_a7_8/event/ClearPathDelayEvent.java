package group_a7_8;

import java.util.ArrayList;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;

public class ClearPathDelayEvent extends SimEvent{
	private PathKey pathKey;
	private double delayFactor;
	public ClearPathDelayEvent(TransitSystem system, Integer eventID, Integer timeRank, PathKey pathKey, double delayFactor) {
    	super(system,timeRank,"clear_path_delay",eventID);
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
		System.out.printf("%s:\n\t%s\n", eventType,toJSON());
		ArrayList<Hazard> pathHazards = system.getHazards(pathKey);
		for(Hazard hazard : pathHazards) {
			if(hazard.getDelayFactor() == delayFactor) {
				pathHazards.remove(hazard);
				break;
			}
		}
		System.out.printf(" %s path delay: %f\n\n",pathKey,system.getPath(pathKey).getDelayFactor());		
	}
	

	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("clearing delay of ");
    	sb.append(delayFactor);
    	sb.append(" for path ");
    	sb.append(pathKey);
    	return sb.toString();
}	
	
}
