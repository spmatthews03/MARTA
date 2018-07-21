package edu.gatech;


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
    }

    public RailCar(int uniqueValue, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        super(uniqueValue, inputRoute,inputLocation,inputPassengers,inputCapacity,inputSpeed);
        this.vehicleType = "Train";
        this.rail_route = this.system.getRailRoute(inputRoute);
        //this.debug_print();
    }
    
    private RailStation get_rail_station(int location_index) {
        RailRoute rail_route = this.get_rail_route();
        int station_id = rail_route.getStationID(location_index);
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
    
    private int get_location_index_current() {
    	return this.getLocation();
    }

    private int get_location_index_next() {
        RailRoute rail_route = this.get_rail_route();
    	int location_index_current = this.get_location_index_current();
        int location_index_next =
        		rail_route.getNextLocation(location_index_current);
        
        return location_index_next;
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
        int location_index_next = this.get_location_index_next();
    	RailStation station = this.get_rail_station(location_index_next);
    	if (station == null) {
    		System.err.print("Error: No next station associated with Train");
    		return null;
    	}
    	
    	return station;
    	
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
