package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;

public class VehicleOutOfServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	private int delta_stall_duration;

	public VehicleOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle, int delta_stall_duration) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.vehicle = vehicle;
		this.outOfService = true;
		this.delta_stall_duration = delta_stall_duration;
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
		System.out.printf(" %s%d is out of service\n",vehicle.getType(),vehicle.getID());

		vehicle.set_delta_stall_duration(this.delta_stall_duration);
		System.out.printf(" %s%d delta stall duration is %d\n\n",vehicle.getType(),vehicle.getID(), this.delta_stall_duration);
	}

	@Override
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(vehicle.getType());
    	sb.append(" ");
    	sb.append(vehicle.getID());
    	sb.append(" is out of service");
    	return sb.toString();	
	}	
	
}
