package group_a7_8;

import edu.gatech.Bus;
import edu.gatech.BusRoute;
import edu.gatech.SimEvent;
import edu.gatech.Stop;
import edu.gatech.TransitSystem;

public class MoveBusEvent extends SimEvent{

    private Bus bus;

	public MoveBusEvent(TransitSystem system, int inputID, int inputRank, Bus bus) {
    	super(system,inputRank,"move_bus",inputID);
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
    	sb.append("\",\"bus\":");
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
        BusRoute activeRoute = system.getRoute(activeBus.getRouteID());
        System.out.println(" the bus is driving on route: " + Integer.toString(activeRoute.getID()));

        int activeLocation = activeBus.getLocation();
        int activeStopID = activeRoute.getStopID(activeLocation);
        Stop activeStop = system.getStop(activeStopID);
        System.out.println(" the bus is currently at stop: " + Integer.toString(activeStop.getID()) + " - " + activeStop.getName());

        // drop off and pickup new passengers at current stop
        int currentPassengers = activeBus.getPassengers();
        int passengerDifferential = activeStop.exchangeRiders(getRank(), currentPassengers, activeBus.getCapacity());
        System.out.println(" passengers pre-stop: " + Integer.toString(currentPassengers) + " post-stop: " + (currentPassengers + passengerDifferential));
        activeBus.adjustPassengers(passengerDifferential);

        // determine next stop
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStopID = activeRoute.getStopID(nextLocation);
        Stop nextStop = system.getStop(nextStopID);
        System.out.println(" the bus is heading to stop: " + Integer.toString(nextStopID) + " - " + nextStop.getName() + "\n");

        // find distance to stop to determine next event time
        Double travelDistance = activeStop.findDistance(nextStop);
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = 1 + (travelDistance.intValue() * 60 / activeBus.getSpeed());
        activeBus.setLocation(nextLocation);

        // generate next event for this bus
        eventQueue.add(new MoveBusEvent(system, eventID, getRank() + travelTime,bus));               
		
	}

}
