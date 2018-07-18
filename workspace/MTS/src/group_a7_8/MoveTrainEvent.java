package group_a7_8;

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

    private int get_next_next_location() {
        RailCar activeTrain = this.get_current_train();
    	RailRoute activeRoute = get_current_route();

        int activeLocation = activeTrain.getLocation();
        int nextLocation = activeRoute.get_next_next_location(activeLocation);

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

    private RailStation get_next_next_station() {
        RailCar activeTrain = this.get_current_train();
    	RailRoute activeRoute = get_current_route();

        int activeLocation = activeTrain.getLocation();
        int nextLocation = activeRoute.get_next_next_location(activeLocation);
        int nextStationID = activeRoute.getStationID(nextLocation);
        RailStation nextStation = system.getRailStation(nextStationID);
        System.out.println(" the bus is heading to station: " + Integer.toString(nextStationID) + " - " + nextStation.getFacilityName() + "\n");

        return nextStation;
	}

    private void drop_off_passengers() {
        /* Drop off and pickup new passengers at current station */
        RailCar activeTrain = this.get_current_train();
        RailStation activeStation = this.get_current_station();
        int currentPassengers = activeTrain.getPassengers();
        int currentRiders = activeTrain.getPassengers();

        activeStation.add_riders(currentPassengers);
        activeTrain.setPassengers(0);

        System.out.println(" Passenger Drop off " +
        				   " pre-train passangers: "	+ currentPassengers +
        				   " post-train passangers:"	+ activeTrain.getPassengers() +  
        				   " pre-station riders: "      + currentRiders +
        				   " post-station riders: "     + activeStation.get_riders());
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

        RailStation nextStation = this.get_next_station(); /* Determine next station */
        PathKey current_pathkey = system.getPathKey(activeStation, nextStation);
        Path current_path = system.getPath(current_pathkey);

        if (activeTrain.getOutOfService()) {
        	if (current_path.getIsBlocked()) {
        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;

                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
        	} else {
        		this.drop_off_passengers();

        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;
        		
        		current_path.setIsBlocked();
        		System.out.printf(" %s path is blocked\n\n", current_pathkey);

            	BlockPathEvent clear_block_path_event =
            			new BlockPathEvent(this.system, eventQueue.getNextEventID(), absolute_stall_period, activeTrain);
            	System.out.printf("%s\n", clear_block_path_event.toJSON());
            	eventQueue.add(clear_block_path_event);
        	}
        } else { /* Train is in service */
            if (current_path.getIsBlocked()) { /* Path is blocked */
        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;

                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
            } else { /* Path is not blocked */
            	if (activeStation.get_out_of_service()) { /* Station is out of service */
            		this.move_train_skip_station();
            	} else { /* Station is in service */
                	this.drop_off_and_pick_up_passengers();
                	this.move_train_next_station();
            	}
            }
        }
	}
	
	private void move_train_next_station() {
        RailCar activeTrain = this.get_current_train();
		RailStation activeStation = this.get_current_station();
        RailStation nextStation = this.get_next_station();

        // Find distance to station to determine next event time
        Double travelDistance = activeStation.findDistance(nextStation);
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = 1 + (travelDistance.intValue() * 60 / activeTrain.getSpeed());
        int nextLocation = this.get_next_location();
        activeTrain.setLocation(nextLocation);
        
		eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));
	}

	private void move_train_skip_station() {
        RailCar activeTrain = this.get_current_train();
		RailStation activeStation = this.get_current_station();
        RailStation nextStation = this.get_next_next_station();

        // Find distance to station to determine next event time
        Double travelDistance = activeStation.findDistance(nextStation);
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = 1 + (travelDistance.intValue() * 60 / activeTrain.getSpeed());
        int nextLocation = this.get_next_next_location();
        activeTrain.setLocation(nextLocation);
        
		eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));
	}
}
