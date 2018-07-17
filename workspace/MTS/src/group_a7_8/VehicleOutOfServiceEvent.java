package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;

public class VehicleOutOfServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	public VehicleOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.vehicle = vehicle;
		this.outOfService = true;
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
		
		vehicle.setOutOfService(outOfService);
		System.out.printf(" %s%d is out of service\n\n",vehicle.getType(),vehicle.getID());
	}
	

	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(vehicle.getType());
    	sb.append(" ");
    	sb.append(vehicle.getID());
    	sb.append(" is out of service");
    	return sb.toString();	
	}	
	
}
