package group_a7_8;

import java.util.ArrayList;

import edu.gatech.Stop;
import edu.gatech.TransitSystem;

public class Path {

	private PathKey pathKey;
	private Integer speedLimit;
	private TransitSystem system;

	
	public Path(TransitSystem system, PathKey pathKey) {
		super();
		this.pathKey = pathKey;
		this.system = system;
	}

	public Path(TransitSystem system,Stop origin, Stop destination) {
		this(system, new PathKey(origin, destination));
	}

	public Integer getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}
	public void clearSpeedLimit() {
		speedLimit = null;
	}
	
	public Stop getOrigin() {
		return pathKey.getOrigin();
	}
	public Stop getDestination() {
		return pathKey.getDestination();
	}
	public double getDistance() {
		return Math.sqrt(Math.pow(((double)getDestination().getYCoord()  - (double)getOrigin().getYCoord()),2.0) + 
				Math.pow(((double)getDestination().getXCoord()  - (double)getOrigin().getXCoord()),2.0));
	}
	public ArrayList<Hazard> getHazards(){
		return system.getHazards(pathKey);
	}
	public double getDelayFactor() {
		double delayFactor = 1.0;
		ArrayList<Hazard> hazards = getHazards();
		if(hazards!=null && hazards.size()>0) {
			for(Hazard hazard : hazards) {
				delayFactor = delayFactor*hazard.getDelayFactor();
			}
		}
		return delayFactor;
	}
	
	@Override
	public String toString() {
		return pathKey.toString();
	}
	
	public String toJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"pathKey\":");
		sb.append(pathKey.toJSON());
		if(getSpeedLimit()!=null) {
			sb.append(",\"speedLimit\":");
			sb.append(this.speedLimit);
		}
		ArrayList<Hazard> hazards = system.getHazards(pathKey);
		if(hazards!=null && hazards.size()>0) {
			sb.append(",\"hazards\":[");
			boolean isFirst = true;
			for(Hazard hazard : hazards) {
				if(isFirst) {
					isFirst = !isFirst;
				}
				else {
					sb.append(',');
				}
				sb.append(hazard.toJSON());
			}
			sb.append("]");
		}
		sb.append('}');
		return sb.toString();
	}
	
}
