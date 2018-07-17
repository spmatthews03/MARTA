package group_a7_8;

import edu.gatech.SimEvent;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;

public class VehicleResumeServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	private TransitSystem system;
	public VehicleResumeServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle) {
    	super(system,timeRank,"set_vehicle_outOfService",eventID);
		this.vehicle = vehicle;
		this.system = system;
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
		
		vehicle.setOutOfService(outOfService);
		System.out.printf(" %s%d resumed service\n\n",vehicle.getType(),vehicle.getID());
		
		if (vehicle.type == "Bus"){
			BusStop stop = system.getBusRoute(vehicle.getRouteID()).getBusStop(system, 0);
			double distanceFromDepot = system.getDepot().findDistance(stop);
			int travelTime = 1 + (distanceFromDepot.intValue() * 60 / vehicle.getSpeed());
			MoveBusEvent moveEvent = new MoveBusEvent(system, this.getID(), this.getRank() + travelTime, vehicle);
			this.getEventQueue().add(moveEvent)
		} else if (vehicle.type == "Train"){
			RailStation station = system.getRailRoute(vehicle.getRouteID()).getRailStation (system, 0);
			double distanceFromDepot = system.getDepot().findDistance(station);
			int travelTime = 1 + (distanceFromDepot.intValue() * 60 / vehicle.getSpeed());
			MoveTrainEvent moveEvent = new MoveTrainEvent(system, this.getID(), this.getRank() + travelTime, vehicle);
			his.getEventQueue().add(moveEvent)
		}		
	}
	
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(vehicle.getType());
    	sb.append(" ");
    	sb.append(vehicle.getID());
    	sb.append(" is back in service");
    	return sb.toString();	
	}	

}
