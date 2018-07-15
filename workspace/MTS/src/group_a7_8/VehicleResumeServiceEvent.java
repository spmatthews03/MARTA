package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;

public class VehicleResumeServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	public VehicleResumeServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.vehicle = vehicle;
		this.outOfService = false;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public boolean getOutOfService() {
		return outOfService;
	}
	@Override
	public void execute() {
		displayEvent();
		System.out.printf(" %s:\n\t%s\n", eventType,toJSON());
		
		//Need setter in vehicle class
		/*
		vehicle.setOutOfService(outOfService);
		System.out.printf(" %s%d resumed service\n\n",vehicle.vehicleType,vehicle.getID());
		*/
	}
	
	public String toJSON() {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append('{');
	    	sb.append("\"ID\":");
	    	sb.append(eventID);
	    	sb.append(",\"time\":");
	    	sb.append(timeRank);
	    	sb.append(",\"type\":\"");
	    	sb.append(eventType);
	    	sb.append("\",\"vehicleType\":");
	    	sb.append(vehicle.vehicleType);
	    	sb.append(",\"vehicleID\":");
	    	sb.append(vehicle.getID());
	    	sb.append(",\"outOfService\":");
	    	sb.append(outOfService);
	    	sb.append('}');
	    	return sb.toString();
	}	
}