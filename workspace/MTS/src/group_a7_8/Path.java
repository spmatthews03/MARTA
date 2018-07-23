package group_a7_8;

import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.TransitSystem;

public class Path {

	private PathKey pathKey;
	private Double speedLimit;
	private boolean isBlocked;
	private TransitSystem system;
	private int delta_stall_duration;

	
	public Path(TransitSystem system, PathKey pathKey) {
		super();
		this.pathKey = pathKey;
		this.system = system;
		this.isBlocked = false;
		this.delta_stall_duration = 0;
	}

	public Path(TransitSystem system,Facility origin, Facility destination) {
		this(system, new PathKey(origin, destination));
	}
	
	//public void addDelay(double delayFactor) {
	//	if(getHazards()==null) {
			
	//	}
	//}

    public int get_delta_stall_duration() {
    	return this.delta_stall_duration;
	}

    public void set_delta_stall_duration(int stall_duration) {
    	this.delta_stall_duration = stall_duration;
	}

	public Double getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(double speedLimit) {
		this.speedLimit = speedLimit;
	}
	public void clearSpeedLimit() {
		speedLimit = null;
	}
	
	public Boolean getIsBlocked() {
		return this.isBlocked;
	}
	public void setIsBlocked() {
		this.isBlocked = true;
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

	public double get_travel_distance() {
		Facility facility_current = this.getOrigin();
		Facility facility_next = this.getDestination();
		Double travel_distance = facility_current.findDistance(facility_next);

		return travel_distance;
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
			//System.out.printf("hazard set for %s is null\n",pathKey);
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
		sb.append(",\"blocked\":");
		sb.append((this.isBlocked?"true":"false"));
		sb.append(",\"speedLimit\":");
	    sb.append((this.speedLimit==null?-1:this.speedLimit));
		sb.append(",\"delayfactor\":");
		sb.append(getDelayFactor());
		sb.append('}');
		return sb.toString();
	}

}