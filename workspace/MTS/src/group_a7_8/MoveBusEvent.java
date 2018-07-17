package group_a7_8;

import edu.gatech.Bus;
import edu.gatech.BusRoute;
import edu.gatech.SimEvent;
import edu.gatech.BusStop;
import edu.gatech.TransitSystem;

public class MoveBusEvent extends SimEvent{

    private Bus bus;

	public MoveBusEvent(TransitSystem system, int eventID, int timeRank, Bus bus) {
    	super(system,timeRank,"move_bus",eventID);
    	this.bus = bus;

    }

    public Bus getBus() { return this.bus; }
    

	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" moving");
    	sb.append(bus.getType());
    	sb.append(" ");
    	sb.append(bus.getID());
    	return sb.toString();	
	}	
	@Override
	public void execute() {
        displayEvent();

        // identify the bus that will move
        Bus activeBus = getBus();
        System.out.println(" the bus being observed is: " + Integer.toString(activeBus.getID()));


        // identify the current stop
        BusRoute activeRoute = system.getBusRoute(activeBus.getRouteID());
        System.out.println(" the bus is driving on route: " + Integer.toString(activeRoute.getID()));

        int activeLocation = activeBus.getLocation();
        int activeStopID = activeRoute.getStopID(activeLocation);
        BusStop activeStop = system.getBusStop(activeStopID);
        System.out.println(" the bus is currently at stop: " + Integer.toString(activeStop.get_uniqueID()) + " - " + activeStop.getFacilityName());

        // determine next stop
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStopID = activeRoute.getStopID(nextLocation);
        BusStop nextStop = system.getBusStop(nextStopID);
        System.out.println(" the bus is heading to stop: " + Integer.toString(nextStopID) + " - " + nextStop.getFacilityName() + "\n");

        // find distance to stop to determine next event time
        Double distanceToNextStopThenDepot = activeStop.findDistance(nextStop) + nextStop.findDistance(system.getDepot());

        if(!activeBus.getOutOfService() && activeBus.hasEnoughFuel(distanceToNextStopThenDepot)) {

            // check if bus has enough fuel to go to next stop then depot, if not, go directly to depot
            int travelTime = 1 + (distanceToNextStopThenDepot.intValue() * 60 / activeBus.getSpeed());

            // Create a fuel report to next stop
            FuelConsumption report = new FuelConsumption(activeBus, new PathKey(activeStop, nextStop),
                    travelTime, activeStop.findDistance(nextStop));
            system.getFuelConsumptionList(activeBus).add(report);

            // drop off and pickup new passengers at current stop
            int currentPassengers = activeBus.getPassengers();
            int passengerDifferential = activeStop.exchangeRiders(getRank(), currentPassengers, activeBus.getCapacity());
            System.out.println(" passengers pre-stop: " + Integer.toString(currentPassengers) + " post-stop: " + (currentPassengers + passengerDifferential));
            activeBus.adjustPassengers(passengerDifferential);

            // conversion is used to translate time calculation from hours to minutes
            activeBus.setLocation(nextLocation);

            // generate next event for this bus
            eventQueue.add(new MoveBusEvent(system, eventID, getRank() + travelTime,bus));
        }
        else if(activeBus.getOutOfService()){
            // calculate distance to depot
            Double distanceToDepot = activeStop.findDistance(system.getDepot());
            int travelTime = 1 + (distanceToDepot.intValue() * 60 / activeBus.getSpeed());

            // create fuel report for traveling to depot
            FuelConsumption report = new FuelConsumption(activeBus, new PathKey(activeStop, system.getDepot()),
                    travelTime, distanceToDepot);
            system.getFuelConsumptionList(activeBus).add(report);

            // drop all passengers before moving to depot
            int dropAllPassengers = activeBus.getPassengers();
            activeBus.adjustPassengers(-(dropAllPassengers));

            // set location to first stop and next location to 2nd stop
            activeBus.setLocation(activeRoute.getStopID(0));
            activeRoute.getNextLocation(1);
        }
        else{
            // calculate distance to depot
            Double distanceToDepot = activeStop.findDistance(system.getDepot());
            int travelTime = 1 + (distanceToDepot.intValue() * 60 / activeBus.getSpeed());

            // create fuel report for traveling to depot
            FuelConsumption report = new FuelConsumption(activeBus, new PathKey(activeStop, system.getDepot()),
                    travelTime, distanceToDepot);
            system.getFuelConsumptionList(activeBus).add(report);

            // drop all passengers before moving to depot
            int dropAllPassengers = activeBus.getPassengers();
            activeBus.adjustPassengers(-(dropAllPassengers));

            // set location to next location
            activeBus.setLocation(nextLocation);

            // TODO: create fuel report for traveling FROM Depot TO next stop

            // create event to set vehicle out of service to refuel
            eventQueue.add(new VehicleOutOfServiceEvent(system,eventID,getRank(),bus));

            // create event to resume service after refueling
            eventQueue.add(new VehicleResumeServiceEvent(system,eventID,getRank()+travelTime,bus));

        }
	}
}

