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
    private HashMap<Integer, BusRoute> routes;
    private HashMap<Integer, Bus> buses;
    private Hashtable<PathKey, Path> paths;
    private Hashtable<PathKey, ArrayList<Hazard>> hazards;
    private StateChangeListener listener;


    public TransitSystem() {
        stops = new HashMap<Integer, Stop>();
        routes = new HashMap<Integer, BusRoute>();
        buses = new HashMap<Integer, Bus>();
        paths = new Hashtable<PathKey,Path>();
        hazards = new Hashtable<PathKey,ArrayList<Hazard>>();
    }
    
    public void setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
    }

    public Stop getStop(int stopID) {
        if (stops.containsKey(stopID)) { return stops.get(stopID); }
        return null;
    }

    public BusRoute getRoute(int routeID) {
        if (routes.containsKey(routeID)) { return routes.get(routeID); }
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

    public int makeRoute(int uniqueID, int inputNumber, String inputName) {
        // int uniqueID = routes.size();
        routes.put(uniqueID, new BusRoute(uniqueID, inputNumber, inputName));
        listener.updateState();
        return uniqueID;
    }

    public int makeBus(int uniqueID, int inputRoute, int inputLocation, int inputPassengers, int inputCapacity, int inputSpeed) {
        // int uniqueID = buses.size();
        buses.put(uniqueID, new Bus(uniqueID, inputRoute, inputLocation, inputPassengers, inputCapacity, inputSpeed));
        listener.updateState();
        return uniqueID;
    }

    public void appendStopToRoute(int routeID, int nextStopID) { 
    	
//    	routes.get(routeID).addNewStop(nextStopID);
		BusRoute route = routes.get(routeID);
		route.addNewStop(nextStopID);

    	//if this is the second or subsequent stop being added, define a path between the new stop and the prior stop
    	if(route.getLength()>1) {
    		System.out.printf("Route %d-%s now has %d stops, adding paths ...\n", route.getID(),route.getName(),route.getLength());
    		
    		//create the path between tbe latest stop and the prior one
    		//by construction, the latest stop will have index n-1, and the other one will have index n-2
    		int latestStopRouteIndex = route.getLength()-1;
    		int priorStopRouteIndex = route.getLength()-2;
    		int latestStopID = route.getStopID(latestStopRouteIndex);
    		int priorStopID = route.getStopID(priorStopRouteIndex);
    		Stop latestStop = getStop(latestStopID);
    		Stop priorStop = getStop(priorStopID);
    		PathKey path1= new PathKey(priorStop, latestStop);
    		paths.put(path1, new Path(this,path1));
    		System.out.printf("Added path %s to route %d-%s\n", path1,route.getID(),route.getName());
    		//the routes are such that they loop from the last stop to the beginning
    		//add that path too
    		int beginStopRouteIndex = 0;
    		int beginStopID = route.getStopID(beginStopRouteIndex);
    		Stop beginStop = getStop(beginStopID);
    		PathKey path2= new PathKey(latestStop,beginStop);
    		paths.put(path2, new Path(this,path2));
    		System.out.printf("Added path %s to route %d-%s\n", path2,route.getID(),route.getName());
    		//now the path from the prior stop to the begin stop is no longer valid.  remove it
    		if(route.getLength()>2) {
	    		PathKey pathToRemove = new PathKey(priorStop,beginStop);
	    		for(PathKey pathKey : paths.keySet()) {
	    			if(pathKey.equals(pathToRemove)) {
	        			paths.remove(pathKey);
	        			System.out.printf("Removed path %s from route %d-%s\n", pathToRemove,route.getID(),route.getName());
	        			break;
	    			}
	    		}    		
    		}
    	}
    	listener.updateState();
    }

    public void addHazard(PathKey pathKey,double delayFactor) {
    	if(!hazards.contains(pathKey)) {
    		hazards.put(pathKey, new ArrayList<Hazard>());
    	}
    	hazards.get(pathKey).add(new Hazard(pathKey,delayFactor));
    }

    public void clearHazard(PathKey pathKey,double delayFactor) {
    	if(hazards.contains(pathKey)) {
    		ArrayList<Hazard> pathHazards = hazards.get(pathKey);
    		for(Hazard hazard : pathHazards) {
    			if(hazard.getDelayFactor()==delayFactor) {
    				pathHazards.remove(hazard);
    			}
    		}
    	}
    }
    
    public void setSpeedLimit(PathKey pathKey,int speedLimit) {
    	if(paths.contains(pathKey)) {
    		paths.get(pathKey).setSpeedLimit(speedLimit);
    	}
    }

    public void clearSpeedLimit(PathKey pathKey) {
    	if(paths.contains(pathKey)) {
    		paths.get(pathKey).clearSpeedLimit();
    	}
    }

    public HashMap<Integer, Stop> getStops() { return stops; }

    public HashMap<Integer, BusRoute> getRoutes() { return routes; }

    public HashMap<Integer, Bus> getBuses() { return buses; }

    public Hashtable<PathKey, Path> getPaths() { return paths; }
    
    public ArrayList<Hazard> getHazards(PathKey pathKey){
    	if(hazards.contains(pathKey)) {
    		return hazards.get(pathKey);
    	}
    	return null;
    }
    
    
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
            	Integer prevStop = routes.get(m.getRouteID()).getStopID(m.getPastLocation());
            	Integer nextStop = routes.get(m.getRouteID()).getStopID(m.getLocation());
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
    	if(sb.length()>1) sb.append(',');
    	if(routes!=null && routes.size()>0) {
        	sb.append("\"routes\":[");    
        	boolean isFirst = true;
        	for(BusRoute route : routes.values()) {
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
    	if(sb.length()>1) sb.append(',');
    	if(stops!=null && stops.size()>0) {
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

    	
    	sb.append('}');
    	return sb.toString();
    }
    
}
