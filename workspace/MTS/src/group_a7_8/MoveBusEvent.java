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
    
    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"ID\":");
    	sb.append(eventID);
    	sb.append(",\"time\":");
    	sb.append(timeRank);
    	sb.append(",\"type\":\"");
    	sb.append(eventType);
    	sb.append("\",\"vehicle\":");
    	sb.append(bus.toJSON());
    	sb.append('}');
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

        // drop off and pickup new passengers at current stop
        int currentPassengers = activeBus.getPassengers();
        int passengerDifferential = activeStop.exchangeRiders(getRank(), currentPassengers, activeBus.getCapacity());
        System.out.println(" passengers pre-stop: " + Integer.toString(currentPassengers) + " post-stop: " + (currentPassengers + passengerDifferential));
        activeBus.adjustPassengers(passengerDifferential);

        // determine next stop
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStopID = activeRoute.getStopID(nextLocation);
        BusStop nextStop = system.getBusStop(nextStopID);
        System.out.println(" the bus is heading to stop: " + Integer.toString(nextStopID) + " - " + nextStop.getFacilityName() + "\n");

        // find distance to stop to determine next event time
        Double distanceToNextStopThenDepot = activeStop.findDistance(nextStop) + nextStop.findDistance(system.getDepot());
        int travelTime = 1 + (distanceToNextStopThenDepot.intValue() * 60 / activeBus.getSpeed());

        // check if bus has enough fuel to go to next stop then depot, if not, go directly to depot
        if(activeBus.hasEnoughFuel(distanceToNextStopThenDepot)){
            FuelConsumption report = new FuelConsumption(activeBus, new PathKey(activeStop, nextStop),
                    travelTime, activeStop.findDistance(nextStop));
            system.getFuelConsumptionList(activeBus).add(report);
        }
        else{
            FuelConsumption report = new FuelConsumption(activeBus, new PathKey(activeStop, nextStop),
                    travelTime, activeStop.findDistance(system.getDepot()));
            system.getFuelConsumptionList(activeBus).add(report);
        }

        // conversion is used to translate time calculation from hours to minutes
        activeBus.setLocation(nextLocation);


        // generate next event for this bus
        eventQueue.add(new MoveBusEvent(system, eventID, getRank() + travelTime,bus));               
		
	}

}
