package group_a7_8;

import edu.gatech.Bus;
import edu.gatech.BusStop;
import edu.gatech.RailCar;
import edu.gatech.RailStation;
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
		
		if (vehicle.getType().equals("Bus")){

			// get bus
			Bus bus = system.getBus(vehicle.getID());

			// refuel and unset refueling flag
			bus.refuel();
			bus.setRefueling(false);

			// set stop
			BusStop stop = system.getBusRoute(vehicle.getRouteID()).getBusStop(system, vehicle.getLocation());

			// find distance from depot
			double distanceFromDepot = system.getDepot().findDistance(stop);
			int travelTime = 1 + (int) (distanceFromDepot * 60 / vehicle.getSpeed());

			// create fuel report from depot to next stop
			FuelConsumption report = new FuelConsumption(bus, new PathKey(system.getDepot(), stop),
					(timeRank + travelTime), distanceFromDepot);
			system.getFuelConsumptionList(bus).add(report);

			// create move bus event
			MoveBusEvent moveEvent = new MoveBusEvent(system, this.getID(), (int)(this.getRank() + travelTime), (Bus)vehicle);
			this.getEventQueue().add(moveEvent);

		} else if (vehicle.getType().equals("Train")) {
			
			vehicle.setOutOfService(false); /* Train is in service */
			
			vehicle.set_prevLocation(0);
			vehicle.set_nextLocation(1);
			RailStation station = system.getRailRoute(vehicle.getRouteID()).getRailStation (system, 0);
			double distanceFromDepot = system.getDepot().findDistance(station);
			int travelTime = 1 + (int)(distanceFromDepot * 60 / vehicle.getSpeed());
			MoveTrainEvent moveEvent = new MoveTrainEvent(system, this.getID(), this.getRank() + travelTime, (RailCar
					)vehicle);
			this.getEventQueue().add(moveEvent);
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
