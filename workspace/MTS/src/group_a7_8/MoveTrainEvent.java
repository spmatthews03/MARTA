package group_a7_8;

import java.util.Hashtable;

import edu.gatech.ExchangePoint;
import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.SimEvent;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;

public class MoveTrainEvent extends SimEvent{

    private RailCar train;

	public MoveTrainEvent(TransitSystem system, int eventID, int timeRank, RailCar train) {
    	super(system,timeRank,"move_bus",eventID);
    	this.train = train;
    }

    public RailCar get_current_train() { return this.train; }
    
    private RailRoute get_current_route() {
        RailCar activeTrain = get_current_train();
        RailRoute activeRoute = system.getRailRoute(activeTrain.getRouteID());
    	return activeRoute;
	}

    private RailStation get_current_station() {
        RailCar activeTrain = this.get_current_train();
        RailRoute activeRoute = this.get_current_route();
    	
        int activeLocation = activeTrain.getLocation();
        int activeStationID = activeRoute.getStationID(activeLocation);
        RailStation activeStation = system.getRailStation(activeStationID);

        return activeStation;
	}
    
    private int get_next_location() {
        RailCar activeTrain = this.get_current_train();
    	RailRoute activeRoute = get_current_route();

        int activeLocation = activeTrain.getLocation();
        int nextLocation = activeRoute.getNextLocation(activeLocation);

        return nextLocation;
    }

    private RailStation get_next_station() {
        RailCar activeTrain = this.get_current_train();
    	RailRoute activeRoute = get_current_route();

        int activeLocation = activeTrain.getLocation();
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        int nextStationID = activeRoute.getStationID(nextLocation);
        RailStation nextStation = system.getRailStation(nextStationID);
        System.out.println(" the bus is heading to station: " + Integer.toString(nextStationID) + " - " + nextStation.getFacilityName() + "\n");

        return nextStation;
	}

    private void drop_off_and_pick_up_passengers() {
        /* Drop off and pickup new passengers at current station */
        RailCar activeTrain = this.get_current_train();
        RailStation activeStation = this.get_current_station();

        int currentPassengers = activeTrain.getPassengers();
        int passengerDifferential = activeStation.exchangeRiders(getRank(), currentPassengers, activeTrain.getCapacity());
        activeTrain.adjustPassengers(passengerDifferential);

        System.out.println(" passengers pre-station: " + Integer.toString(currentPassengers) + " post-station: " + (currentPassengers + passengerDifferential));
    }

    @Override
    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"ID\":");
    	sb.append(eventID);
    	sb.append(",\"time\":");
    	sb.append(timeRank);
    	sb.append(",\"type\":\"");
    	sb.append(eventType);
    	sb.append("\",\"description\":\"");
    	sb.append(getDescription());
    	sb.append("\",");
    	sb.append("\"vehicle\":{");
    	  sb.append("\"type\":\"");
    	  sb.append(get_current_train().getType());
    	  sb.append("\",\"ID\":");
    	  sb.append(get_current_train().getID());
    	sb.append('}');
    	sb.append('}');
    	return sb.toString();    	
    }
    
	public String getDescription() {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" moving");
    	sb.append(train.getType());
    	sb.append(" ");
    	sb.append(train.getID());
    	return sb.toString();	
	}	

	@Override
	public void execute() {
        displayEvent();

        // identify the train that will move
        RailCar activeTrain = get_current_train();
        System.out.println(" the train being observed is: " + Integer.toString(activeTrain.getID()));

        // identify the current station
        RailRoute activeRoute = this.get_current_route();
        System.out.println(" the train is moving on rail: " + Integer.toString(activeRoute.getID()));

        RailStation activeStation = this.get_current_station();
        System.out.println(" the Train is currently at station: " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName());

        if (activeTrain.getOutOfService()) {
        	/*
        	// Drop off passengers
            int currentPassengers = activeTrain.getPassengers();
            activeTrain.adjustPassengers(-currentPassengers);
            
            // Move train to the beginning of the route
            activeTrain.setLocation(activeRoute.getStationID(0));
            activeRoute.getNextLocation(1);
            
        	int travelTime = 99;
            eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));
			*/
        } else { /* Train is in service */
            RailStation nextStation = this.get_next_station(); /* Determine next station */
            PathKey current_pathkey = system.getPathKey(activeStation, nextStation);
            Path current_path = system.getPath(current_pathkey);
            if (current_path.getIsBlocked()) { /* Path is blocked */
            	/* Wait until path block clears by creating move_train event after stall duration */
            	int travelTime = 1 + current_path.get_stallDuration();
                eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));
            } else { /* Path is not blocked */
            	this.drop_off_and_pick_up_passengers();

                /* Find distance to station to determine next event time */
                Double travelDistance = activeStation.findDistance(nextStation);
                // conversion is used to translate time calculation from hours to minutes
                int travelTime = 1 + (travelDistance.intValue() * 60 / activeTrain.getSpeed());
                int nextLocation = this.get_next_location();
                activeTrain.setLocation(nextLocation);

                /* Generate next event for this bus */
                eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));           	
            }
        }       
	}
}
