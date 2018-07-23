package group_a7_8.event;

import edu.gatech.RailStation;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import group_a7_8.PathKey;

public class VehicleOutOfServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	private int delta_stall_duration;
	private int repairDuration;

	public VehicleOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle, int delta_stall_duration, int repairDuration) {
    	super(system,timeRank,"vehicle_out_of_service",eventID);
		this.vehicle = vehicle;
		this.outOfService = true;
		this.delta_stall_duration = delta_stall_duration;
		this.repairDuration = repairDuration;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	public boolean getOutOfService() {
		return outOfService;
	}
	public int getStallDuration() {
		return delta_stall_duration;
	}
	public int getRepairDuration() {
		return repairDuration;
	}

	@Override
	public void execute() {
		displayEvent();
		//System.out.printf(" %s:\n\t%s\n", eventType,toJSON());

		vehicle.setOutOfService(outOfService);
		System.out.printf(" %s%d is out of service\n",vehicle.getType(),vehicle.getID());
		
		if(vehicle.getType().equals("Train")) {
			RailStation lastStation = system.getRailStation(vehicle.getPastLocation());
			RailStation nextStation = system.getRailStation(vehicle.getLocation());
			PathKey currentPathKey = system.getPathKey(lastStation, nextStation);
			system.getPath(currentPathKey).set_delta_stall_duration(delta_stall_duration);
		} else {
			vehicle.set_delta_stall_duration(this.delta_stall_duration);
		}
		//System.out.printf(" %s%d delta stall duration is %d\n\n",vehicle.getType(),vehicle.getID(), this.delta_stall_duration);
		
		vehicle.setRepairDuration(this.repairDuration);
		//System.out.printf(" %s%d has a repair duration of %d\n\n",vehicle.getType(),vehicle.getID(), this.repairDuration);
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