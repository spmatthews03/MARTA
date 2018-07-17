package group_a7_8;

import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.TransitSystem;

public class Path {

	private PathKey pathKey;
	private Integer speedLimit;
	private boolean isBlocked;
	private int stallDuration;
	private TransitSystem system;

	
	public Path(TransitSystem system, PathKey pathKey) {
		super();
		this.pathKey = pathKey;
		this.system = system;
		this.isBlocked = false;
		this.stallDuration = 0;
	}

	public Path(TransitSystem system,Facility origin, Facility destination) {
		this(system, new PathKey(origin, destination));
	}
	
	public void addDelay(double delayFactor) {
		if(getHazards()==null) {
			
		}
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
	
	public Boolean getIsBlocked() {
		return this.isBlocked;
	}
	public int get_stallDuration() {
		return this.stallDuration;
	}
	public void setIsBlocked(int stallDuration) {
		this.isBlocked = true;
		this.stallDuration = stallDuration;
	}
	public void clearIsBlocked() {
		this.isBlocked = false;
	}
	
	public PathKey getPathKey() { return pathKey;}
	
	public Facility getOrigin() {
		return pathKey.getOrigin();
	}
	public Facility getDestination() {
		return pathKey.getDestination();
	}
	public double getDistance() {
		return Math.sqrt(Math.pow(((double)getDestination().getLocation().getX()  - (double)getOrigin().getLocation().getY()),2.0) + 
				Math.pow(((double)getDestination().getLocation().getX()  - (double)getOrigin().getLocation().getX()),2.0));
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
		else {
			System.out.printf("hazard set for %s is null\n",pathKey);
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
