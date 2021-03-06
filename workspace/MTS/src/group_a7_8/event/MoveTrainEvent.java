package group_a7_8.event;

import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.RailStation;
import edu.gatech.TransitSystem;
import group_a7_8.Path;
import group_a7_8.PathKey;

public class MoveTrainEvent extends SimEvent {

    private RailCar train;
    public boolean skip_station;

	public MoveTrainEvent(TransitSystem system, int eventID, int timeRank, RailCar train) {
    	super(system,timeRank,"move_train",eventID);
    	this.train = train;
    	this.skip_station = false;
    	//this.debug_print();
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

    private void drop_off_passengers() {
        /* Drop off and pickup new passengers at current station */
        RailCar activeTrain = this.get_current_train();
        RailStation activeStation = activeTrain.get_rail_station_current();
        int currentPassengers = activeTrain.getPassengers();
        int currentRiders = activeTrain.getPassengers();

        activeStation.add_riders(currentPassengers);
        activeTrain.setPassengers(0);
/*
        System.out.println(" MoveTrainEvent:  Passenger Drop off " +
        				   " pre-train passangers: "	+ currentPassengers +
        				   " post-train passangers:"	+ activeTrain.getPassengers() +  
        				   " pre-station riders: "      + currentRiders +
        				   " post-station riders: "     + activeStation.get_riders());
*/
    }

    private void drop_off_and_pick_up_passengers() {
        /* Drop off and pickup new passengers at current station */
        RailCar activeTrain = this.get_current_train();
        RailStation activeStation = activeTrain.get_rail_station_current();

        int currentPassengers = activeTrain.getPassengers();
        int passengerDifferential = activeStation.exchangeRiders(getRank(), currentPassengers, activeTrain.getCapacity());
        activeTrain.adjustPassengers(passengerDifferential);
        
        System.out.println(" passengers pre-stop: " + Integer.toString(currentPassengers) + " post-stop: " + (currentPassengers + passengerDifferential));
        //System.out.println(" MoveTrainEvent: Drop off & pick up passengers pre-station: " + Integer.toString(currentPassengers) + " post-station: " + (currentPassengers + passengerDifferential));
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
        	System.err.println("Error MoveTrain Event " + this.eventID + ": No train available");
        	return;
        }
        System.out.println(" the train being observed is: " + Integer.toString(activeTrain.getID()));
        //System.out.println(" MoveTrainEvent: Train " + Integer.toString(activeTrain.getID()));

        // identify the current station
        RailRoute activeRoute = this.get_current_route();
        if (activeRoute == null) {
        	System.err.println("Error MoveTrain Event " + this.eventID + ": No train route available");
        	return;
        }
        System.out.println(" the train is running on route: " + Integer.toString(activeRoute.getID()));
        //System.out.println(" MoveTrainEvent: on rail: " + Integer.toString(activeRoute.getID()));

        RailStation activeStation = activeTrain.get_rail_station_current();
        if (activeStation == null) {
        	System.err.println("Error MoveTrain Event " + this.eventID + ": No current station available");
        	return;
        }
        System.out.println(" the train is currently at station: " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName());
        //System.out.println(" MoveTrainEvent: currently at station: " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName());

        RailStation nextStation = activeTrain.get_rail_station_next(); /* Determine next station */
        if (nextStation == null) {
        	System.err.println("Error MoveTrain Event " + this.eventID + ": No next station available");
        	return;
        }
        System.out.println(" the train is heading to station: " + Integer.toString(nextStation.get_uniqueID()) + " - " + nextStation.getFacilityName() + "\n");
        //System.out.println("activeStation: " + activeStation.toJSON());
        //System.out.println("nextStation: "   + nextStation.toJSON());
        
        PathKey current_pathkey = system.getPathKey(activeStation, nextStation);
        Path current_path = system.getPath(current_pathkey);
        //System.out.println(" MoveTrainEvent: moving to station: " + nextStation.get_uniqueID() + " - " + nextStation.getFacilityName());

        //System.out.println(activeStation.getFacilityName() + " -> " + nextStation.getFacilityName());

        if (activeTrain.getOutOfService()) {
        	System.out.println(" train " + Integer.toString(activeTrain.getID()) + " is going out of service and headed to the depot for repair\n");
            //System.out.println(" MoveTrainEvent: Train out of service");
        	if (current_path.getIsBlocked()) {
                //System.out.println(" MoveTrainEvent: Path is blocked");
                //System.out.println(" MoveTrainEvent: \t" + current_path.toJSON());
        		int delta_stall_period = current_path.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;
        		System.out.println(" path: " + current_path.toString() + " is blocked\n");
                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
                System.out.println(" train " + Integer.toString(activeTrain.getID()) + " is waiting to go into station " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName() + "\n");
                //System.out.println(activeStation.getFacilityName() + " -> " + nextStation.getFacilityName());
        	} else {
                //System.out.println(" MoveTrainEvent: Train is NOT blocked");
        		this.drop_off_passengers();

        		int delta_stall_period = current_path.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;
        		//System.out.println(" train " + activeTrain.getID() + " is stalling at " + current_path.toString() + " for " + current_path.get_delta_stall_duration());
        		
        		current_path.setIsBlocked();
        		System.out.println(" train " + Integer.toString(activeTrain.getID()) + " is blocking path: " + current_path.toString() + "\n");
            	BlockPathEvent clear_block_path_event =
            			new BlockPathEvent(this.system, eventQueue.getNextEventID(), absolute_stall_period, activeTrain, current_path);
            	System.out.printf("%s\n", clear_block_path_event.toJSON());
            	eventQueue.add(clear_block_path_event);
        	}
        } else { /* Train is in service */
            //System.out.println(" MoveTrainEvent: Train is in service");
            if (current_path.getIsBlocked()) { /* Path is blocked */
                //System.out.println(" MoveTrainEvent: Path is blocked");
            	//System.out.println(" path: " + activeStation.getFacilityName() + " -> " + nextStation.getFacilityName() + " is blocked\n");
        		int delta_stall_period = current_path.get_delta_stall_duration();
        		Integer absolute_stall_period = getRank() + delta_stall_period;
        		System.out.println(" train " + Integer.toString(activeTrain.getID()) + " is waiting to go into station " + Integer.toString(activeStation.get_uniqueID()) + " - " + activeStation.getFacilityName() + "\n");
        		//System.out.println(" Train " + activeTrain.getID() + "Going to Depot");
                eventQueue.add(
                		new MoveTrainEvent(system, eventID,
                						   absolute_stall_period , train));
            } else {
            	/* Path after traveling the current path */
                Path next_path = activeTrain.get_path_next();
            	if (next_path.getIsBlocked()) {
            		int delta_stall_period = next_path.get_delta_stall_duration();
            		Integer absolute_stall_period = getRank() + delta_stall_period;
            		System.out.println(" path out of station " + Integer.toString(nextStation.get_uniqueID()) + " - " + nextStation.getFacilityName() + " is blocked\n");
            		//Integer absolute_stall_period = getRank() + 1;//delta_stall_period;
            		System.out.println(" train " + Integer.toString(activeTrain.getID()) + " is waiting to go into station " + Integer.toString(nextStation.get_uniqueID()) + " - " + nextStation.getFacilityName() + "\n");
            		//System.out.println(" Train " + activeTrain.getID() + "Waiting on blocked next next path");
                    eventQueue.add(
                    		new MoveTrainEvent(system, eventID,
                    						   absolute_stall_period , train));	
            	} else { /* Path is not blocked */
                    //System.out.println(" MoveTrainEvent: Path is NOT blocked");

                	if (nextStation.get_out_of_service()) { /* Station is out of service */  
                        //System.out.println(" MoveTrainEvent: Station out of service");
                		System.out.println(" station " + activeStation.get_uniqueID() + "is out of service");
                        //System.out.println(" Skipping " + nextStation.getFacilityName());
                		this.train_move_next_next(train);
                	} else { /* Station is in service */
                		//System.out.println(" MoveTrainEvent: Station in service");

                		this.drop_off_and_pick_up_passengers();
                		this.train_move_next(train);
                	}            		
            	}
            }
        }
	}

	private void train_move_next(RailCar my_train) {
		int travel_time = my_train.calculate_travel_time_station_next();
		
		my_train.advance_station_location();
        MoveTrainEvent my_move_event = new MoveTrainEvent(system, eventID, getRank() + travel_time, train);

        eventQueue.add(my_move_event);
	}
	
	private void train_move_next_next(RailCar my_train) {
		int travel_time_station_next = my_train.calculate_travel_time_station_next();
		int travel_time_station_next_next = my_train.calculate_travel_time_station_next();
		int travel_time_total = travel_time_station_next + travel_time_station_next_next;
		int travel_time_total_absolute = getRank() + travel_time_total;

		my_train.advance_station_location();
		my_train.advance_station_location();
        MoveTrainEvent my_move_event = new MoveTrainEvent(system, eventID, travel_time_total_absolute, train);

        eventQueue.add(my_move_event);
	}
}
