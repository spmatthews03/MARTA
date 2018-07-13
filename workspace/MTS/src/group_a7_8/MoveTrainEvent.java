package group_a7_8;

import edu.gatech.Bus;
import edu.gatech.BusRoute;
import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.SimEvent;
import edu.gatech.Stop;
import edu.gatech.TransitSystem;

public class MoveTrainEvent extends SimEvent{

    private RailCar train;

	public MoveTrainEvent(TransitSystem system, int eventID, int timeRank, RailCar train) {
    	super(system,timeRank,"move_bus",eventID);
    	this.train = train;

    }

    public RailCar getTrain() { return this.train; }
    
    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"ID\":");
    	sb.append(eventID);
    	sb.append(",\"time\":");
    	sb.append(timeRank);
    	sb.append(",\"type\":\"");
    	sb.append(eventType);
    	sb.append("\",\"train\":");
    	sb.append(train.toJSON());
    	sb.append('}');
    	return sb.toString();
    }

	@Override
	public void execute() {
        displayEvent();

        // identify the train that will move
        RailCar activeTrain = getTrain();
        System.out.println(" the train being observed is: " + Integer.toString(activeTrain.getID()));

        // identify the current stop
        RailRoute activeRoute = system.getRailRoute(activeTrain.getRouteID());
        System.out.println(" the bus is driving on route: " + Integer.toString(activeRoute.getID()));

        int activeLocation = activeTrain.getLocation();
        int activeStopID = activeRoute.getStationID(activeLocation);
        Stop activeStop = system.getStop(activeStopID);  //TODO needs to be getStation() when that is available
         System.out.println(" the Train is currently at stop: " + Integer.toString(activeStop.getID()) + " - " + activeStop.getName());

        // drop off and pickup new passengers at current stop
        int currentPassengers = activeTrain.getPassengers();
        int passengerDifferential = activeStop.exchangeRiders(getRank(), currentPassengers, activeTrain.getCapacity());
        System.out.println(" passengers pre-stop: " + Integer.toString(currentPassengers) + " post-stop: " + (currentPassengers + passengerDifferential));
        activeTrain.adjustPassengers(passengerDifferential);

        // determine next stop
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStopID = activeRoute.getStationID(nextLocation);
        Stop nextStop = system.getStop(nextStopID);
        System.out.println(" the bus is heading to stop: " + Integer.toString(nextStopID) + " - " + nextStop.getName() + "\n");

        // find distance to stop to determine next event time
        Double travelDistance = activeStop.findDistance(nextStop);
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = 1 + (travelDistance.intValue() * 60 / activeTrain.getSpeed());
        activeTrain.setLocation(nextLocation);

        // generate next event for this bus
        eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime,train));               
		
	}

}
