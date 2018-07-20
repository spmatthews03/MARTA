package group_a7_8;

import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.SimEvent;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;

public class MoveTrainEvent extends SimEvent{

    private RailCar train;
    public boolean skip_station;

	public MoveTrainEvent(TransitSystem system, int eventID, int timeRank, RailCar train) {
    	super(system,timeRank,"move_train",eventID);
    	this.train = train;
    	this.skip_station = false;
    	this.debug_print();
    }
	
	public void skip_next_station() {
		this.skip_station = true;
	}

	protected void debug_print () {
		System.out.println(" " + this.getClass().getName() + " Instantiated");
		System.out.println("\t" + this.toJSON());
	}

    public RailCar get_current_train() { return this.train; }
    
    private RailRoute get_current_route() {
        RailCar activeTrain = get_current_train();
        Integer train_route_id = activeTrain.getRouteID();
        RailRoute activeRoute = system.getRailRoute(train_route_id);
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

        return nextStation;
	}

    private RailStation get_next_next_station() {
        RailCar activeTrain = this.get_current_train();
    	RailRoute activeRoute = get_current_route();

        int activeLocation = activeTrain.getLocation();
        int nextLocation = activeRoute.get_next_next_location(activeLocation);
        int nextStationID = activeRoute.getStationID(nextLocation);
        RailStation nextStation = system.getRailStation(nextStationID);

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

        System.out.println(" MoveTrainEvent:  Passenger Drop off " +
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

        System.out.println(" MoveTrainEvent: Drop off & pick up passengers pre-station: " + Integer.toString(currentPassengers) + " post-station: " + (currentPassengers + passengerDifferential));
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
        if (activeTrain == null) {
        	System.out.println("Error MoveTrain Event " + this.eventID + ": No train available");
        	return;
        }
        System.out.println(" MoveTrainEvent: Train " + Integer.toString(activeTrain.getID()));

        // identify the current station
        RailRoute activeRoute = this.get_current_route();
        if (activeRoute == null) {
        	System.out.println("Error MoveTrain Event " + this.eventID + ": No train route available");
        	return;
        }
        System.out.println(" MoveTrainEvent: on rail: " + Integer.toString(activeRoute.getID()));

        RailStation activeStation = this.get_current_station();
        if (activeStation == null) {
        	System.out.println("Error MoveTrain Event " + this.eventID + ": No station available");
        	return;
        }
        System.out.println(" MoveTrainEvent: currently at station: " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName());

        RailStation nextStation = this.get_next_station(); /* Determine next station */
        PathKey current_pathkey = system.getPathKey(activeStation, nextStation);
        Path current_path = system.getPath(current_pathkey);
        System.out.println(" MoveTrainEvent: moving to station: " +
        				   nextStation.get_uniqueID() + " - " +
        				   nextStation.getFacilityName());

        if (activeTrain.getOutOfService()) {
            System.out.println(" MoveTrainEvent: Train out of service");
        	if (current_path.getIsBlocked()) {
                System.out.println(" MoveTrainEvent: Path is blocked");
                System.out.println(" MoveTrainEvent: \t" + current_path.toJSON());
        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;

                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
        	} else {
                System.out.println(" MoveTrainEvent: Train is NOT blocked");
        		this.drop_off_passengers();

        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;
        		
        		current_path.setIsBlocked();

            	BlockPathEvent clear_block_path_event =
            			new BlockPathEvent(this.system, eventQueue.getNextEventID(), absolute_stall_period, activeTrain);
            	System.out.printf("%s\n", clear_block_path_event.toJSON());
            	eventQueue.add(clear_block_path_event);
        	}
        } else { /* Train is in service */
            System.out.println(" MoveTrainEvent: Train is in service");
            if (current_path.getIsBlocked()) { /* Path is blocked */
                System.out.println(" MoveTrainEvent: Path is blocked");
                System.out.println(" MoveTrainEvent: \t" + current_path.toJSON());

        		int delta_stall_period = activeTrain.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + 1;//delta_stall_period;

                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
            } else { /* Path is not blocked */
                System.out.println(" MoveTrainEvent: Path is NOT blocked");
                System.out.println(" MoveTrainEvent: \t" + current_path.toJSON());

            	if (nextStation.get_out_of_service() && !this.skip_station) { /* Station is out of service && 
            																	moveStation is not suppose to skip a station */
                    System.out.println(" MoveTrainEvent: Station out of service");
                    System.out.println(" MoveTrainEvent: \t" + activeStation.toJSON());

            		this.move_train_skip_station();
            	} else { /* Station is in service */
            		if (this.skip_station) {
                        System.out.println(" MoveTrainEvent: Skipping stations");
            		} else {
                        System.out.println(" MoveTrainEvent: Station in service");
                    	this.drop_off_and_pick_up_passengers();
            		}

                    System.out.println(" MoveTrainEvent: \t" + activeStation.toJSON());
                	this.move_train_next_station(false);
            	}
            }
        }
	}
	
	private void move_train_next_station(boolean skip_next_station) {
        RailCar activeTrain = this.get_current_train();
		RailStation activeStation = this.get_current_station();
        RailStation nextStation = this.get_next_station();

        Path currentPath = system.getPath(system.getPathKey(activeStation, nextStation));
        
        // get delay factor
        Double delayfactor = currentPath.getDelayFactor();
        
        // calculate the effect of speed limit
        Integer speedlimit = currentPath.getSpeedLimit();
        Integer true_speed = activeTrain.getSpeed();
        if (speedlimit != null) {
        	if (speedlimit < true_speed) {
        		true_speed = speedlimit;
        	}
        }
        
        // Find distance to station to determine next event time
        Double travelDistance = activeStation.findDistance(nextStation);    
       
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = (int)((1 + (travelDistance.intValue() * 60 / true_speed)) * delayfactor);
        
        RailRoute activeRoute = system.getRailRoute(train.getRouteID());
        int activeLocation = train.getLocation();
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        activeTrain.setLocation(nextLocation);
        
        MoveTrainEvent my_move_event = new MoveTrainEvent(system, eventID, getRank() + travelTime, train);
		if (skip_next_station) {
			my_move_event.skip_next_station();
		}

        eventQueue.add(my_move_event);
	}

	private void move_train_skip_station() {
		boolean skip_next_station = true;
		this.move_train_next_station(skip_next_station);

        RailCar activeTrain = this.get_current_train();
		RailStation activeStation = this.get_current_station();
		RailStation nextStation = this.get_next_station();
        RailStation nextNextStation = this.get_next_next_station();

        Path currentPath = system.getPath(system.getPathKey(activeStation, nextStation));
        Path nextPath = system.getPath(system.getPathKey(nextStation, nextNextStation));
        
        // get delay factor
        Double currentdelayfactor = currentPath.getDelayFactor();
        Double nextdelayfactor = nextPath.getDelayFactor();
        
        // calculate the effect of speed limit
        Integer currentspeedlimit = currentPath.getSpeedLimit();
        Integer current_true_speed = activeTrain.getSpeed();
        if (currentspeedlimit != null) {
        	if (currentspeedlimit < current_true_speed) {
        		current_true_speed = currentspeedlimit;
        	}
        }
        Integer nextcurrentspeedlimit = nextPath.getSpeedLimit();
        Integer next_true_speed = activeTrain.getSpeed();
        if (nextcurrentspeedlimit != null) {
        	if (nextcurrentspeedlimit < next_true_speed) {
        		next_true_speed = nextcurrentspeedlimit;
        	}
        }
        
        // Find distance to station to determine next event time
        Double currentTravelDistance = activeStation.findDistance(nextStation);
        Double nextTravelDistance = nextStation.findDistance(nextNextStation);
       
        // conversion is used to translate time calculation from hours to minutes
        int travelTime = (int)(((1 + (currentTravelDistance.intValue() * 60 / current_true_speed)) * currentdelayfactor) 
        		+ ((1 + (nextTravelDistance.intValue() * 60 / next_true_speed)) * nextdelayfactor)) ;
        
        RailRoute activeRoute = system.getRailRoute(train.getRouteID());
        int activeLocation = train.getLocation();
        int nextLocation = activeRoute.getNextLocation(activeLocation);
        activeTrain.setLocation(nextLocation);
        
		eventQueue.add(new MoveTrainEvent(system, eventID, getRank() + travelTime, train));
	}
}
