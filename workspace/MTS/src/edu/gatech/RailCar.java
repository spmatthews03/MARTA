package edu.gatech;

import group_a7_8.MoveTrainEvent;
import group_a7_8.Path;
import group_a7_8.PathKey;

public class RailCar extends Vehicle {

	protected RailRoute rail_route;

    public RailCar() {
        this.ID = -1;
        this.vehicleType = "Train";
    }

    public RailCar(int uniqueValue) {
        super(uniqueValue);
        this.vehicleType = "Train";
        this.rail_route = null;
        this.system = null;
    }
    
    private void check_references() {
    	if (this.rail_route == null) {
    		System.err.print("Error: No rail route associated with train");
    	}
    	if (this.system == null) {
    		System.err.print("Error: No transit system associated with train");
    	}
    }
    public RailCar(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
        this.vehicleType = "Train";
        
        // Convert from station id given in input to location index

        //this.debug_print();
    }
    
    public void set_rail_route_and_location_index(RailRoute my_rail_route) {
        if (my_rail_route == null) {
        	System.err.println("Error: Train has invalid rail route");
        }

    	this.rail_route = my_rail_route;

        Integer location_index = this.get_location_index_current();

        this.set_location_index_current(location_index);
        

        location_index = this.rail_route.getNextLocation(location_index);
        if (location_index == null) {
        	System.err.println("Error: Train starting next location is invalid");
        }

        this.set_location_index_next(location_index);
    }
    
    private RailStation get_rail_station(int location_index) {
    	this.check_references();

        RailRoute rail_route = this.get_rail_route();
        Integer station_id = rail_route.getStationID(location_index);
        if (station_id == null) {
        	System.err.println("Error: Train has an invalid location index " + location_index);
        }
        RailStation station = system.getRailStation(station_id);
        
        return station;
    }

    private RailRoute get_rail_route() {
    	if (this.rail_route == null) {
    		System.err.print("Error: No route associated with Train");
    		return null;
    	}
    	return this.rail_route;
    }

    private void set_location_index_current(int location_index) {
    	this.set_prevLocation(location_index);
    }

    private void set_location_index_next(int location_index) {
    	this.set_nextLocation(location_index);
    }
    
    public void advance_station_location() {
        int location_index_next = this.get_location_index_next();
    	int location_index_next_next = this.get_location_index_next_next();
        
        this.set_location_index_current(location_index_next);
        this.set_location_index_next(location_index_next_next);
    }

    private int get_location_index_current() {
    	return this.getPastLocation();
    }

    private int get_location_index_next() {
        RailRoute rail_route = this.get_rail_route();
    	int location_index_current = this.get_location_index_current();
        int location_index_next =
        		rail_route.getNextLocation(location_index_current);
        
        return location_index_next;
    }

    private int get_location_index_next_next() {
        RailRoute rail_route = this.get_rail_route();
        int location_index_next = this.get_location_index_next();
        int location_index_next_next = 
        		rail_route.getNextLocation(location_index_next);

        return location_index_next_next;
    }

    public RailStation get_rail_station_current() {
        int location_index_current = this.get_location_index_current();
    	RailStation station = this.get_rail_station(location_index_current);
    	if (station == null) {
    		System.err.print("Error: No current station associated with Train");
    		return null;
    	}
    	
    	return station;
    }

    public RailStation get_rail_station_next() {
        int location_index = this.get_location_index_next(); /* 1 station ahead */
    	RailStation station = this.get_rail_station(location_index);
    	if (station == null) {
    		System.err.print("Error: No next station associated with Train");
    		return null;
    	}
    	return station;
    }

    public RailStation get_rail_station_next_next() {
        int location_index = this.get_location_index_next_next(); /* 2 stations ahead */
    	RailStation station = this.get_rail_station(location_index);
    	if (station == null) {
    		System.err.print("Error: No next next station associated with Train");
    		return null;
    	}
    	return station;
    }

    private Path get_path_next() {
    	this.check_references();

		RailStation station_current = this.get_rail_station_current();
        RailStation station_next    = this.get_rail_station_next();
        PathKey path_key = system.getPathKey(station_current, station_next);
        Path path = system.getPath(path_key);

    	return path;
    }

    private Path get_path_next_next() {
    	this.check_references();

		RailStation station_current = this.get_rail_station_next();
        RailStation station_next    = this.get_rail_station_next_next();
        PathKey path_key = system.getPathKey(station_current, station_next);
        Path path = system.getPath(path_key);

    	return path;
    }

    private int calculate_travel_time_path(Path path) {
        Double delay_factor = path.getDelayFactor();
        Integer speed_limit = path.getSpeedLimit();
        Integer true_speed = this.getSpeed();
        Double travel_distance;

        if (speed_limit != null) {
        	if (speed_limit < true_speed) {
        		true_speed = speed_limit;
        	}
        }

        travel_distance = path.get_travel_distance();

        // conversion is used to translate time calculation from hours to minutes
        int travel_time = (int)((1 + (travel_distance.intValue() * 60 / true_speed)) * delay_factor);

        return travel_time;    
    }

    public int calculate_travel_time_station_next() {
    	Path path = this.get_path_next();
    	return calculate_travel_time_path(path);
    }

    public int calculate_travel_time_station_next_next() {
    	Path path = this.get_path_next_next();
    	return calculate_travel_time_path(path);
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"type\":\"");
        sb.append(this.vehicleType);
        sb.append("\",");
        sb.append("\"ID\":");
        sb.append(this.ID);
        sb.append(',');
        sb.append("\"routeID\":");
        sb.append(this.route);
        sb.append(',');
        sb.append("\"capacity\":");
        sb.append(this.capacity);
        sb.append(',');
        sb.append("\"passengers\":");
        sb.append(this.passengers);
        sb.append(',');
        sb.append("\"nextLocation\":");
        sb.append(this.nextLocation);
        sb.append(',');
        sb.append("\"prevLocation\":");
        sb.append(this.prevLocation);
        sb.append(',');
        sb.append("\"speed\":");
        sb.append(this.speed);
        sb.append(',');
        sb.append("\"outOfService\":");
        sb.append((this.getOutOfService()?"true":"false"));        
        sb.append('}');
        return sb.toString();
    }
    public boolean equals(Object object){ return this.equals(object); }

}
