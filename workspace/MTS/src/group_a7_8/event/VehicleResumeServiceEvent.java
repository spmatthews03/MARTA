package group_a7_8.event;

import edu.gatech.Bus;
import edu.gatech.BusStop;
import edu.gatech.Depot;
import edu.gatech.RailCar;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import group_a7_8.FuelConsumption;
import group_a7_8.PathKey;

public class VehicleResumeServiceEvent extends SimEvent{
	private Vehicle vehicle;
	private boolean outOfService;
	private TransitSystem system;
	public VehicleResumeServiceEvent(TransitSystem system, Integer eventID, Integer timeRank, Vehicle vehicle) {
    	super(system,timeRank,"vehicle_resumed_service",eventID);
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
		//System.out.printf("VehicleResumeServiceEvent %s:\n\t%s\n", eventType,toJSON());
		
		vehicle.setOutOfService(outOfService);
		System.out.printf(" %s%d resumed service\n\n",vehicle.getType(),vehicle.getID());
		
		if (vehicle.getType().equals("Bus")){

			// get bus
			Bus bus = system.getBus(vehicle.getID());

			// refuel and unset refueling flag
			if (bus.isRefueling() == true) {
				bus.refuel();
				bus.setRefueling(false);
			}

			// set stop
			BusStop stop = system.getBusRoute(vehicle.getRouteID()).getBusStop(system, vehicle.getLocation());

			// find distance from depot
			double distanceFromDepot = system.getDepot().findDistance(stop);
			System.out.printf(" distance from depot to first stop is %f\n\n",distanceFromDepot);
			
			int travelTime = 1 + (int) (distanceFromDepot * 60 / vehicle.getSpeed());

			// create fuel report from depot to next stop
			FuelConsumption report = new FuelConsumption(bus, new PathKey(system.getDepot(), stop),
					(timeRank + travelTime), distanceFromDepot, bus.getPassengers());
			system.getFuelConsumptionList(bus).add(report);

			// create move bus event
			MoveBusEvent moveEvent = new MoveBusEvent(system, this.getID(), (int)(this.getRank() + travelTime), (Bus)vehicle);
			this.getEventQueue().add(moveEvent);
			
			bus.setTowDuration(null);

		} else if (vehicle.getType().equals("Train")) {
			
			//vehicle.setOutOfService(false); /* Train is in service */
			
			RailStation station = system.getRailRoute(vehicle.getRouteID()).getRailStation (system, vehicle.getLocation());
			Depot my_depot = system.getDepot();
	        if (my_depot  == null) {
	        	System.out.println("Error VehicleResumeService Event " + this.eventID + ": No depot has been created");
	        	return;
	        }
			double distanceFromDepot = my_depot.findDistance(station);
			System.out.printf(" distance from depot to first station is %f\n\n",distanceFromDepot);
			int travelTime = 1 + (int)(distanceFromDepot * 60 / vehicle.getSpeed());
			MoveTrainEvent moveEvent = new MoveTrainEvent(system, this.getID(), this.getRank() + travelTime, (RailCar
					)vehicle);
			this.getEventQueue().add(moveEvent);
		}	
			
		vehicle.setRepairDuration(0);
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
