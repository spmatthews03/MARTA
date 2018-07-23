package group_a7_8.event;

import edu.gatech.Bus;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import group_a7_8.PathKey;

public class VehicleOutOfServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	private int stallDuration;
	private int repairDuration;

	public VehicleOutOfServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle, int stallDuration, int repairDuration) {
    	super(system,timeRank,"vehicle_out_of_service",eventID);
		this.vehicle = vehicle;
		this.outOfService = true;
		this.stallDuration = stallDuration;
		this.repairDuration = repairDuration;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	public boolean getOutOfService() {
		return outOfService;
	}
	public int getStallDuration() {
		return stallDuration;
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
			if (currentPathKey != null) { // Invalid path mayb it's the start up station
				system.getPath(currentPathKey).set_delta_stall_duration(stallDuration);
			} else {
				System.out.printf(" %s%d has no path available for setting duration stall\n",
						  vehicle.getType(), vehicle.getID());
			}
		} else {
			Bus activeBus = system.getBus(vehicle.getID());
			activeBus.setTowDuration(stallDuration);;
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
