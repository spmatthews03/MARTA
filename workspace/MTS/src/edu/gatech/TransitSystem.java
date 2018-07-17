package edu.gatech;

import java.util.Hashtable;

import group_a7_8.FuelConsumption;
import group_a7_8.Hazard;
import group_a7_8.Path;
import group_a7_8.PathKey;
import group_a7_8.server.StateChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class TransitSystem {
    private Hashtable<Integer, BusStop> busstops;
    private Hashtable<Integer, RailStation> railstations;
    private Hashtable<Integer, BusRoute> busRoutes;
    private Hashtable<Integer, RailRoute> railRoutes;
    private Hashtable<Integer, Bus> buses;
    private Hashtable<Integer, RailCar> trains;
    private Hashtable<PathKey, Path> paths;
    private Hashtable<PathKey, ArrayList<Hazard>> hazards;
    private Hashtable<Bus, ArrayList<FuelConsumption>> fuelConsumption;
    private StateChangeListener listener;
    private Depot depot;

    public TransitSystem() {
        busstops = new Hashtable<Integer, BusStop>();
        railstations = new Hashtable<Integer, RailStation>();
        busRoutes = new Hashtable<Integer, BusRoute>();
        railRoutes = new Hashtable<Integer, RailRoute>();
        buses = new Hashtable<Integer, Bus>();
        trains = new Hashtable<Integer, RailCar>();
        paths = new Hashtable<PathKey,Path>();
        hazards = new Hashtable<PathKey,ArrayList<Hazard>>();
        depot = null;
        fuelConsumption = new Hashtable<Bus, ArrayList<FuelConsumption>>();
    }

    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    public ArrayList<FuelConsumption> getFuelConsumptionList(Bus bus){
    	if(!fuelConsumption.containsKey(bus)) { fuelConsumption.put(bus, new ArrayList<FuelConsumption>());} 
    		return fuelConsumption.get(bus);
    	
	}

	public double getTotalFuelConsumed(Bus bus){
    	double sum = 0;
    	if(fuelConsumption.containsKey(bus)) {
    		for(FuelConsumption val : fuelConsumption.get(bus)){
    			sum += val.getFuelConsumed();
			}
		}
		return sum;
	}

    public BusStop getBusStop(int stopID) {
        if (busstops.containsKey(stopID)) { return busstops.get(stopID); }
        return null;
    }

    public RailStation getRailStation(int railStationID) {
        if (railstations.containsKey(railStationID)) { return railstations.get(railStationID); }
        return null;
    }

    public RailRoute getRailRoute(int routeID) {
        if (railRoutes.containsKey(routeID)) { return railRoutes.get(routeID); }
        return null;
    }

    public BusRoute getBusRoute(int routeID) {
        if (busRoutes.containsKey(routeID)) { return busRoutes.get(routeID); }
        return null;
    }
    
    public Bus getBus(int busID) {
        if (buses.containsKey(busID)) { return buses.get(busID); }
        return null;
    }

    public RailCar getTrain(int trainID) {
    	if (trains.containsKey(trainID)) { return trains.get(trainID); }
    	return null;
	}

    public Depot getDepot() {
    	return this.depot;
	}

    public Path getPath(PathKey pathKey) {
        if (paths.containsKey(pathKey)) { return paths.get(pathKey); }
		return null;
    }

    public int makeStop(int uniqueID, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
        // int uniqueID = stops.size();
        busstops.put(uniqueID, new BusStop(uniqueID, inputName, inputRiders, inputXCoord, inputYCoord));
        listener.updateState();
        return uniqueID;
    }

    public int makeRailStation(int uniqueID, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
        railstations.put(uniqueID, new RailStation(uniqueID, inputName, inputRiders, inputXCoord, inputYCoord));
        listener.updateState();
        return uniqueID;
    }

    public int makeBusRoute(int uniqueID, int inputNumber, String inputName) {
        // int uniqueID = routes.size();
    	busRoutes.put(uniqueID, new BusRoute(uniqueID, inputNumber, inputName));
        listener.updateState();
        return uniqueID;
    }
    
    public int makeRailRoute(int uniqueID, int inputNumber, String inputName) {
        // int uniqueID = routes.size();
    	railRoutes.put(uniqueID, new RailRoute(uniqueID, inputNumber, inputName));
        listener.updateState();
        return uniqueID;
    }

    public int makeBus(int uniqueID, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity,
					   double fuelLevel, double fuelCapacity, int inputSpeed) {
        // int uniqueID = buses.size();
        buses.put(uniqueID, new Bus(this, uniqueID, inputRoute, inputLocation, inputPassengers, inputCapacity, fuelLevel, fuelCapacity, inputSpeed));
        listener.updateState();
        return uniqueID;
    }

	public int makeDepot(int uniqueID, String name, int x_coord, int y_coord) {
		depot = new Depot(uniqueID, name, x_coord, y_coord);
		listener.updateState();
		return uniqueID;
	}

    public int makeTrain(int uniqueID, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
    	trains.put(uniqueID, new RailCar(uniqueID, inputRoute, inputLocation, inputPassengers, inputCapacity, inputSpeed));
		listener.updateState();
		return uniqueID;
	}

    public void appendStopToRoute(int routeID, int nextStopID) { 	
//    	routes.get(routeID).addNewStop(nextStopID);
		//System.out.printf("routeId: %d extended with stopId: %d\n", routeID,nextStopID);
		BusRoute busRoute = busRoutes.get(routeID);
		//System.out.printf("busRoute: %s\n", busRoute);
		busRoute.addNewStop(nextStopID);
		//System.out.printf("busRoute: %s\n", busRoute);

    	//if this is the second or subsequent stop being added, define a path between the new stop and the prior stop
    	if(busRoute.getLength()>1) {
    		//System.out.printf("busRoute id: %s\n", busRoute.getID());
    		//System.out.printf("busRoute name: %s\n", busRoute.getName());
    		//System.out.printf("busRoute length: %s\n", busRoute.getLength());
    		//System.out.printf("Route %d-%s now has %d stops, adding paths ...\n", busRoute.getID(),busRoute.getName(),busRoute.getLength());
    		
    		//create the path between the latest stop and the prior one
    		//by construction, the latest stop will have index n-1, and the other one will have index n-2
    		int latestStopRouteIndex = busRoute.getLength()-1;
    		int priorStopRouteIndex = busRoute.getLength()-2;
    		//System.out.printf("latestStopRouteIndex : %s\n", latestStopRouteIndex);
    		//System.out.printf("priorStopRouteIndex : %s\n", priorStopRouteIndex);
    		int latestStopID = busRoute.getStopID(latestStopRouteIndex);
    		int priorStopID = busRoute.getStopID(priorStopRouteIndex);
    		//System.out.printf("latestStopID: %s\n", latestStopID);
    		//System.out.printf("priorStopID: %s\n", priorStopID);
    		BusStop latestStop = getBusStop(latestStopID);
    		BusStop priorStop = getBusStop(priorStopID);
    		//System.out.printf("latestStop: %s\n", latestStop);
    		//System.out.printf("priorStop: %s\n", priorStop);
    		PathKey path1= new PathKey(priorStop, latestStop);
    		//System.out.printf("path1: %s\n", path1);
    		paths.put(path1, new Path(this,path1));
    		//System.out.printf("Added path %s to route %d-%s\n", path1,busRoute.getID(),busRoute.getName());
    		
    		//the routes are such that they loop from the last stop to the beginning
    		//add that path too
    		int beginStopRouteIndex = 0;
    		int beginStopID = busRoute.getStopID(beginStopRouteIndex);
    		BusStop beginStop = getBusStop(beginStopID);
    		PathKey path2= new PathKey(latestStop,beginStop);
    		paths.put(path2, new Path(this,path2));
    		//System.out.printf("Added path %s to route %d-%s\n", path2,busRoute.getID(),busRoute.getName());
    		
    		//now the path from the prior stop to the begin stop is no longer valid.  remove it
    		if(busRoute.getLength()>2) {
	    		PathKey pathToRemove = new PathKey(priorStop,beginStop);
	    		for(PathKey pathKey : paths.keySet()) {
	    			if(pathKey.equals(pathToRemove)) {
	        			paths.remove(pathKey);
	        			//System.out.printf("Removed path %s from route %d-%s\n", pathToRemove,busRoute.getID(),busRoute.getName());
	        			break;
	    			}
	    		}    		
    		}
    	}
    	listener.updateState();
    }
    
    public void appendStationToRoute(int routeID, int nextStationID) { 
		RailRoute railRoute = railRoutes.get(routeID);
		railRoute.addNewStation(nextStationID);

    	//if this is the second or subsequent station being added, define a path between the new station and the prior station
    	if(railRoute.getLength()>1) {
    		//System.out.printf("Route %d-%s now has %d stops, adding paths ...\n", railRoute.getID(),railRoute.getName(),railRoute.getLength());
    		
    		//create the path between the latest station and the prior one
    		//by construction, the latest station will have index n-1, and the other one will have index n-2
    		int latestStationRouteIndex = railRoute.getLength()-1;
    		int priorStationRouteIndex = railRoute.getLength()-2;
    		int latestStationID = railRoute.getStationID(latestStationRouteIndex);
    		int priorStationID = railRoute.getStationID(priorStationRouteIndex);
    		RailStation latestStation = getRailStation(latestStationID);
    		RailStation priorStation = getRailStation(priorStationID);
    		PathKey path1= new PathKey(priorStation, latestStation);
    		paths.put(path1, new Path(this,path1));
    		//System.out.printf("Added path %s to route %d-%s\n", path1,railRoute.getID(),railRoute.getName());
    		
    		//the routes are such that they loop from the last station to the beginning
    		//add that path too
    		int beginStationRouteIndex = 0;
    		int beginStationID = railRoute.getStationID(beginStationRouteIndex);
    		RailStation beginStation = getRailStation(beginStationID);
    		PathKey path2= new PathKey(latestStation,beginStation);
    		paths.put(path2, new Path(this,path2));
    		//System.out.printf("Added path %s to route %d-%s\n", path2,railRoute.getID(),railRoute.getName());
    		
    		//now the path from the prior station to the begin station is no longer valid.  remove it
    		if(railRoute.getLength()>2) {
	    		PathKey pathToRemove = new PathKey(priorStation,beginStation);
	    		for(PathKey pathKey : paths.keySet()) {
	    			if(pathKey.equals(pathToRemove)) {
	        			paths.remove(pathKey);
	        			//System.out.printf("Removed path %s from route %d-%s\n", pathToRemove,railRoute.getID(),railRoute.getName());
	        			break;
	    			}
	    		}    		
    		}
    	}
    	listener.updateState();
    }
    
    public void addHazard(PathKey pathKey,double delayFactor) {
    	if(!hazards.containsKey(pathKey)) {
    		hazards.put(pathKey, new ArrayList<Hazard>());
    		//System.out.printf("creating a new set o hazards for %s\n",pathKey);
    	}
    	hazards.get(pathKey).add(new Hazard(pathKey,delayFactor));
    	listener.updateState();
    }
    
    public PathKey getPathKey(ExchangePoint origin, ExchangePoint destination) {
    	PathKey pathKey = null;
    	for(PathKey pk : paths.keySet()) {
    		if(pk.getOrigin()==origin && pk.getDestination()==destination) {
    			pathKey = pk;
    			break;
    		}
    	}
    	return pathKey;
    }
    
    public void clearHazard(PathKey pathKey,double delayFactor) {
    	Path path = getPath(pathKey);
    	if(hazards.contains(path.getPathKey())) {
    		ArrayList<Hazard> pathHazards = hazards.get(path.getPathKey());
    		for(Hazard hazard : pathHazards) {
    			if(hazard.getDelayFactor()==delayFactor) {
    				pathHazards.remove(hazard);
    				listener.updateState();
    				break;
    			}
    		}
    	}
    }
    
    public void setSpeedLimit(PathKey pathKey,int speedLimit) {
    	if(paths.contains(pathKey)) {
    		paths.get(pathKey).setSpeedLimit(speedLimit);
			listener.updateState();
    	}
    }

    public void clearSpeedLimit(PathKey pathKey) {
    	if(paths.contains(pathKey)) {
    		paths.get(pathKey).clearSpeedLimit();
			listener.updateState();
    	}
    }

    public Hashtable<Integer, BusStop> getStops() { return busstops; }
    
    public Hashtable<Integer, RailStation> getRailStations() { return railstations; }

    public Hashtable<Integer, BusRoute> getBusRoutes() { return busRoutes; }
    
    public Hashtable<Integer, RailRoute> getRailRoutes() { return railRoutes; }

    public Hashtable<Integer, Bus> getBuses() { return buses; }

	public Hashtable<Integer, RailCar> getTrains() { return trains;	}

	public Hashtable<PathKey, Path> getPaths() { return paths; }

	public Hashtable<Bus, ArrayList<FuelConsumption>> getFuelConsumption() {
		return fuelConsumption;
	}

	public ArrayList<Hazard> getHazards(PathKey pathKey){
    	if(hazards.containsKey(pathKey)) {
    		return hazards.get(pathKey);
    	}
    	return null;
    }
    
    //Do we need a displayRailModel and a displayBusModel?
    //Current implementation only contain busStops, railStations are not implemented
    public void displayModel() {
    	ArrayList<MiniPair> busNodes, stopNodes;
    	MiniPairComparator compareEngine = new MiniPairComparator();
    	
    	int[] colorScale = new int[] {9, 29, 69, 89, 101};
    	String[] colorName = new String[] {"#000077", "#0000FF", "#000000", "#770000", "#FF0000"};
    	Integer colorSelector, colorCount, colorTotal;
    	
    	try{
            // create new file access path
            String path="./mts_digraph.dot";
            File file = new File(path);

            // create the file if it doesn't exist
            if (!file.exists()) { file.createNewFile();}

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            bw.write("digraph G\n");
            bw.write("{\n");
    	
            busNodes = new ArrayList<MiniPair>();
            for (Bus b: buses.values()) { busNodes.add(new MiniPair(b.getID(), b.getPassengers())); }
            Collections.sort(busNodes, compareEngine);

            colorSelector = 0;
            colorCount = 0;
            colorTotal = busNodes.size();
            for (MiniPair c: busNodes) {
            	if (((int) (colorCount++ * 100.0 / colorTotal)) > colorScale[colorSelector]) { colorSelector++; }
            	bw.write("  bus" + c.getID() + " [ label=\"bus#" + c.getID() + " | " + c.getValue() + " riding\", color=\"" + colorName[colorSelector] + "\"];\n");
            }
            bw.newLine();
            
            stopNodes = new ArrayList<MiniPair>();
            for (BusStop s: busstops.values()) { stopNodes.add(new MiniPair(s.get_uniqueID(), s.get_riders())); }
            Collections.sort(stopNodes, compareEngine);

            colorSelector = 0;
            colorCount = 0;
            colorTotal = stopNodes.size();    	
            for (MiniPair t: stopNodes) {
            	if (((int) (colorCount++ * 100.0 / colorTotal)) > colorScale[colorSelector]) { colorSelector++; }
            	bw.write("  stop" + t.getID() + " [ label=\"stop#" + t.getID() + " | " + t.getValue() + " waiting\", color=\"" + colorName[colorSelector] + "\"];\n");
            }
            bw.newLine();
            
            for (Bus m: buses.values()) {
            	Integer prevStop = busRoutes.get(m.getRouteID()).getStopID(m.getPastLocation());
            	Integer nextStop = busRoutes.get(m.getRouteID()).getStopID(m.getLocation());
            	bw.write("  stop" + Integer.toString(prevStop) + " -> bus" + Integer.toString(m.getID()) + " [ label=\" dep\" ];\n");
            	bw.write("  bus" + Integer.toString(m.getID()) + " -> stop" + Integer.toString(nextStop) + " [ label=\" arr\" ];\n");
            }
    	
            bw.write("}\n");
            bw.close();
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }
    
    public String toJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	

    	if((buses!=null && buses.size()>0) || (trains!=null && trains.size()>0)) {
        	sb.append(serializeVehiclesJSON());
    	}
    	if((busRoutes!=null && busRoutes.size()>0) || (railRoutes!=null && railRoutes.size()>0)) {
        	if(sb.length()>1) sb.append(',');
        	sb.append(serializeRoutesJSON());
    	}
    	if( (busstops!=null && busstops.size()>0) || 
    		(railstations!=null && railstations.size()>0) ||
    		depot != null) {
        	if(sb.length()>1) sb.append(',');
        	sb.append(serializeStopsJSON());
    	}
    	
    	if(paths!=null && paths.size()>0) {
        	if(sb.length()>1) sb.append(',');
        	sb.append("\"paths\":[");    
        	boolean isFirst = true;
        	for(Path path : paths.values()) {
        		if(isFirst) {
        			isFirst = !isFirst;
        		}
        		else {
        			sb.append(',');
        		}
        		sb.append(path.toJSON());
        	}
        	sb.append(']');
    	}
    	
    	sb.append('}');
    	return sb.toString();
    }

	private String serializeStopsJSON() {
    	StringBuilder sb = new StringBuilder();
		sb.append("\"stops\":[");    
    	boolean isFirst = true;
    	for(BusStop stop : busstops.values()) {
    		if(isFirst) {
    			isFirst = !isFirst;
    		}
    		else {
    			sb.append(',');
    		}
    		sb.append(stop.toJSON());
    	}
    	//TODO: restore once JSON methods implemented
//    	for(RailStation stop : railstations.values()) {
//    		if(isFirst) {
//    			isFirst = !isFirst;
//    		}
//    		else {
//    			sb.append(',');
//    		}
//    		sb.append(stop.toJSON());
//    	}
//    	for(Depot stop : depots.values()) {
//    		if(isFirst) {
//    			isFirst = !isFirst;
//    		}
//    		else {
//    			sb.append(',');
//    		}
//    		sb.append(stop.toJSON());
//    	}
    	sb.append(']');
    	return sb.toString();
	}

	private String serializeRoutesJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\"routes\":[");    
    	boolean isFirst = true;
    	for(BusRoute route : busRoutes.values()) {
    		if(isFirst) {
    			isFirst = !isFirst;
    		}
    		else {
    			sb.append(',');
    		}
    		sb.append(route.toJSON());
    	}
    	for(RailRoute route : railRoutes.values()) {
    		if(isFirst) {
    			isFirst = !isFirst;
    		}
    		else {
    			sb.append(',');
    		}
    		sb.append(route.toJSON());
    	}
    	sb.append(']');
    	return sb.toString();
	}

	private String serializeVehiclesJSON() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("\"vehicles\":[");    
    	boolean isFirst = true;
    	for(Bus bus : buses.values()) {
    		if(isFirst) {
    			isFirst = !isFirst;
    		}
    		else {
    			sb.append(',');
    		}
    		sb.append(bus.toJSON());
    	}
    	for(RailCar train : trains.values()) {
    		if(isFirst) {
    			isFirst = !isFirst;
    		}
    		else {
    			sb.append(',');
    		}
    		sb.append(train.toJSON());
    	}
    	sb.append(']');
    	return sb.toString();
	}
    
}
