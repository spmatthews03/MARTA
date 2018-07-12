package edu.gatech;

import java.util.HashMap;
import java.util.Hashtable;

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
    private HashMap<Integer, Stop> stops;
    private Hashtable<Integer, BusRoute> busRoutes;
    private Hashtable<Integer, RailRoute> railRoutes;
    private HashMap<Integer, Bus> buses;
    private Hashtable<PathKey, Path> paths;
    private Hashtable<PathKey, ArrayList<Hazard>> hazards;
    private StateChangeListener listener;
    private HashMap<Integer, Depot> depots;


    public TransitSystem() {
        stops = new HashMap<Integer, Stop>();
        busRoutes = new Hashtable<Integer, BusRoute>();
        railRoutes = new Hashtable<Integer, RailRoute>();
        buses = new HashMap<Integer, Bus>();
        paths = new Hashtable<PathKey,Path>();
        hazards = new Hashtable<PathKey,ArrayList<Hazard>>();
        depots = new HashMap<Integer, Depot>();
    }

    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    public Stop getStop(int stopID) {
        if (stops.containsKey(stopID)) { return stops.get(stopID); }
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

    public Path getPath(PathKey pathKey) {
        if (paths.containsKey(pathKey)) { return paths.get(pathKey); }
		return null;
    }

    public int makeStop(int uniqueID, String inputName, int inputRiders, double inputXCoord, double inputYCoord) {
        // int uniqueID = stops.size();
        stops.put(uniqueID, new Stop(uniqueID, inputName, inputRiders, inputXCoord, inputYCoord));
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

    public int makeBus(int uniqueID, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        // int uniqueID = buses.size();
        buses.put(uniqueID, new Bus(uniqueID, inputRoute, inputLocation, inputPassengers, inputCapacity, inputSpeed));
        listener.updateState();
        return uniqueID;
    }

	public int makeDepot(int uniqueID, String name, int x_coord, int y_coord) {
	    // int uniqueID = buses.size();
		depots.put(uniqueID, new Depot(uniqueID, name, x_coord, y_coord));
		listener.updateState();
	    return uniqueID;
	}

    public void appendStopToRoute(int routeID, int nextStopID) { 	
//    	routes.get(routeID).addNewStop(nextStopID);
		System.out.printf("routeId: %d extended with stopId: %d\n", routeID,nextStopID);
		BusRoute busRoute = busRoutes.get(routeID);
		System.out.printf("busRoute: %s\n", busRoute);
		busRoute.addNewStop(nextStopID);
		System.out.printf("busRoute: %s\n", busRoute);

    	//if this is the second or subsequent stop being added, define a path between the new stop and the prior stop
    	if(busRoute.getLength()>1) {
    		System.out.printf("busRoute id: %s\n", busRoute.getID());
    		System.out.printf("busRoute name: %s\n", busRoute.getName());
    		System.out.printf("busRoute length: %s\n", busRoute.getLength());
    		System.out.printf("Route %d-%s now has %d stops, adding paths ...\n", busRoute.getID(),busRoute.getName(),busRoute.getLength());
    		
    		//create the path between the latest stop and the prior one
    		//by construction, the latest stop will have index n-1, and the other one will have index n-2
    		int latestStopRouteIndex = busRoute.getLength()-1;
    		int priorStopRouteIndex = busRoute.getLength()-2;
    		System.out.printf("latestStopRouteIndex : %s\n", latestStopRouteIndex);
    		System.out.printf("priorStopRouteIndex : %s\n", priorStopRouteIndex);
    		int latestStopID = busRoute.getStopID(latestStopRouteIndex);
    		int priorStopID = busRoute.getStopID(priorStopRouteIndex);
    		System.out.printf("latestStopID: %s\n", latestStopID);
    		System.out.printf("priorStopID: %s\n", priorStopID);
    		Stop latestStop = getStop(latestStopID);
    		Stop priorStop = getStop(priorStopID);
    		System.out.printf("latestStop: %s\n", latestStop);
    		System.out.printf("priorStop: %s\n", priorStop);
    		PathKey path1= new PathKey(priorStop, latestStop);
    		System.out.printf("path1: %s\n", path1);
    		paths.put(path1, new Path(this,path1));
    		System.out.printf("Added path %s to route %d-%s\n", path1,busRoute.getID(),busRoute.getName());
    		
    		//the routes are such that they loop from the last stop to the beginning
    		//add that path too
    		int beginStopRouteIndex = 0;
    		int beginStopID = busRoute.getStopID(beginStopRouteIndex);
    		Stop beginStop = getStop(beginStopID);
    		PathKey path2= new PathKey(latestStop,beginStop);
    		paths.put(path2, new Path(this,path2));
    		System.out.printf("Added path %s to route %d-%s\n", path2,busRoute.getID(),busRoute.getName());
    		
    		//now the path from the prior stop to the begin stop is no longer valid.  remove it
    		if(busRoute.getLength()>2) {
	    		PathKey pathToRemove = new PathKey(priorStop,beginStop);
	    		for(PathKey pathKey : paths.keySet()) {
	    			if(pathKey.equals(pathToRemove)) {
	        			paths.remove(pathKey);
	        			System.out.printf("Removed path %s from route %d-%s\n", pathToRemove,busRoute.getID(),busRoute.getName());
	        			break;
	    			}
	    		}    		
    		}
    	}
    	listener.updateState();
    }
    
    // To be added after implementation of Station Class
    /* 
    public void appendStationToRoute(int routeID, int nextStationID) { 
		RailRoute railRoute = railRoutes.get(routeID);
		railRoute.addNewStation(nextStationID);

    	//if this is the second or subsequent station being added, define a path between the new station and the prior station
    	if(railRoute.getLength()>1) {
    		System.out.printf("Route %d-%s now has %d stops, adding paths ...\n", railRoute.getID(),railRoute.getName(),railRoute.getLength());
    		
    		//create the path between the latest station and the prior one
    		//by construction, the latest station will have index n-1, and the other one will have index n-2
    		int latestStationRouteIndex = railRoute.getLength()-1;
    		int priorStationRouteIndex = railRoute.getLength()-2;
    		int latestStationID = railRoute.getStationID(latestStationRouteIndex);
    		int priorStationID = railRoute.getStationID(priorStationRouteIndex);
    		Station latestStation = getStation(latestStationID);
    		Station priorStation = getStation(priorStationID);
    		PathKey path1= new PathKey(priorStation, latestStation);
    		paths.put(path1, new Path(this,path1));
    		System.out.printf("Added path %s to route %d-%s\n", path1,railRoute.getID(),railRoute.getName());
    		
    		//the routes are such that they loop from the last station to the beginning
    		//add that path too
    		int beginStationRouteIndex = 0;
    		int beginStationID = railRoute.getStationID(beginStationRouteIndex);
    		Stop beginStation = getStation(beginStationID);
    		PathKey path2= new PathKey(latestStation,beginStation);
    		paths.put(path2, new Path(this,path2));
    		System.out.printf("Added path %s to route %d-%s\n", path2,railRoute.getID(),railRoute.getName());
    		
    		//now the path from the prior station to the begin station is no longer valid.  remove it
    		if(railRoute.getLength()>2) {
	    		PathKey pathToRemove = new PathKey(priorStation,beginStation);
	    		for(PathKey pathKey : paths.keySet()) {
	    			if(pathKey.equals(pathToRemove)) {
	        			paths.remove(pathKey);
	        			System.out.printf("Removed path %s from route %d-%s\n", pathToRemove,railRoute.getID(),railRoute.getName());
	        			break;
	    			}
	    		}    		
    		}
    	}
    	listener.updateState();
    }
    */
    
    public void addHazard(PathKey pathKey,double delayFactor) {
    	if(!hazards.containsKey(pathKey)) {
    		hazards.put(pathKey, new ArrayList<Hazard>());
    		//System.out.printf("creating a new set o hazards for %s\n",pathKey);
    	}
    	hazards.get(pathKey).add(new Hazard(pathKey,delayFactor));
    	listener.updateState();
    }

    public PathKey getBusPathKey(int originBusID, int destinationBusID) {
    	PathKey pathKey = null;
    	for(PathKey pk : paths.keySet()) {
    		if(pk.getOrigin().getID()==originBusID && pk.getDestination().getID()==destinationBusID) {
    			pathKey = pk;
    			break;
    		}
    	}
    	return pathKey;
    }
    
    public PathKey getRailPathKey(int originRailID, int destinationRailID) {
    	PathKey pathKey = null;
    	for(PathKey pk : paths.keySet()) {
    		if(pk.getOrigin().getID()==originRailID && pk.getDestination().getID()==destinationRailID) {
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

    public HashMap<Integer, Stop> getStops() { return stops; }

    public Hashtable<Integer, BusRoute> getBusRoutes() { return busRoutes; }
    
    public Hashtable<Integer, RailRoute> getRailRoutes() { return railRoutes; }

    public HashMap<Integer, Bus> getBuses() { return buses; }

    public Hashtable<PathKey, Path> getPaths() { return paths; }
    
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
            for (Stop s: stops.values()) { stopNodes.add(new MiniPair(s.getID(), s.getWaiting())); }
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

    	if(buses!=null && buses.size()>0) {
        	sb.append("\"buses\":[");    
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
        	sb.append(']');
    	}
    	if(busRoutes!=null && busRoutes.size()>0) {
        	if(sb.length()>1) sb.append(',');
        	sb.append("\"busRoutes\":[");    
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
        	sb.append(']');
    	}
    	if(railRoutes!=null && railRoutes.size()>0) {
        	if(sb.length()>1) sb.append(',');
        	sb.append("\"railRoutes\":[");    
        	boolean isFirst = true;
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
    	}
    	if(stops!=null && stops.size()>0) {
        	if(sb.length()>1) sb.append(',');
        	sb.append("\"stops\":[");    
        	boolean isFirst = true;
        	for(Stop stop : stops.values()) {
        		if(isFirst) {
        			isFirst = !isFirst;
        		}
        		else {
        			sb.append(',');
        		}
        		sb.append(stop.toJSON());
        	}
        	sb.append(']');
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

	public Bus getbus(Integer busID) {
		return buses.get(busID);
	}
    
}
