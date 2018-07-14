package edu.gatech;

import java.util.Scanner;

import group_a7_8.ClearPathDelayEvent;
import group_a7_8.FileProps;
import group_a7_8.MoveBusEvent;
import group_a7_8.MoveTrainEvent;
import group_a7_8.PathKey;
import group_a7_8.SetPathDelayEvent;
import group_a7_8.SetSpeedLimitEvent;
import group_a7_8.ClearSpeedLimitEvent;
import group_a7_8.FacilityOutOfServiceEvent;
import group_a7_8.FacilityResumeServiceEvent;
import group_a7_8.VehicleOutOfServiceEvent;
import group_a7_8.VehicleResumeServiceEvent;
import group_a7_8.server.StateChangeListener;
import group_a7_8.server.UpdateManager;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Random;
import java.awt.EventQueue;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SimDriver implements StateChangeListener{
	private final static Object lock = new Object();
    private static SimQueue simEngine;
    private static TransitSystem martaModel;
    private static Random randGenerator;

    public SimDriver() {
        simEngine = new SimQueue();
        simEngine.setStateChangeListener(this);
        martaModel = new TransitSystem();
        martaModel.setStateChangeListener(this);
        randGenerator = new Random();
    }
    
    private final String DELIMITER = ",";
	private UpdateManager setUpdateManager;
	private UpdateManager updateManager;
    public boolean processCommand(String userCommandLine){
    	synchronized(lock) {
    	//System.out.printf("processing command `%s`\n", userCommandLine);
        String[] tokens;
        tokens = userCommandLine.split(DELIMITER);

        System.out.println(tokens[0]);

        switch (tokens[0]) {
            case "add_event":
            	switch(tokens[2]) {
	            	case "move_bus":
	            	{
	                    System.out.print(" new event - rank: " + Integer.parseInt(tokens[1]));
	                    System.out.println(" type: " + tokens[2] + " ID: " + Integer.parseInt(tokens[3]) + " created");
	                    // for this event type
	                    // tokens[1] is the time rank of the event, when it is scheduled to execute
	                    // tokens[2] is the event_type, in this case, move_bus
	                    // tokens[3] is the event ID, it also doubles as the bus ID
	                    Bus bus = martaModel.getBus(Integer.decode(tokens[3]));
	                    MoveBusEvent event = new MoveBusEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), bus);
	                    simEngine.add(event);
	            	}
	                    break;
	            	case "move_train":
	            	{
	                    System.out.print(" new event - rank: " + Integer.parseInt(tokens[1]));
	                    System.out.println(" type: " + tokens[2] + " ID: " + Integer.parseInt(tokens[3]) + " created");
	                    // for this event type
	                    // tokens[1] is the time rank of the event, when it is scheduled to execute
	                    // tokens[2] is the event_type, in this case, move_train
	                    // tokens[3] is the event ID, it also doubles as the train ID
	                    RailCar train = martaModel.getTrain(Integer.decode(tokens[3]));
	                    MoveTrainEvent event = new MoveTrainEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), train);
	                    simEngine.add(event);
	            	}
	                    break;
	                    default:
	                    	System.out.printf("%s is an unrecognized event\n",tokens[2]);
            	}
                break;
            case "add_stop":
                int stopID = martaModel.makeStop(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                System.out.println(" new stop: " + Integer.toString(stopID) + " created");
                break;
            case "add_station":
                int railStationID = martaModel.makeRailStation(Integer.parseInt(tokens[1]), tokens[2], Integer.parseInt(tokens[3]), Double.parseDouble(tokens[4]), Double.parseDouble(tokens[5]));
                System.out.println(" New Rail Station: " + Integer.toString(railStationID) + " created");
                break;
            case "add_route":
                int busRouteID = martaModel.makeBusRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3]);
                System.out.println(" new bus route: " + Integer.toString(busRouteID) + " created");
                break;
            case "add_train_route":
                int railRouteID = martaModel.makeRailRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3]);
                System.out.println(" new rail route: " + Integer.toString(railRouteID) + " created");
                break;
            case "add_bus":
                int busID = martaModel.makeBus(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Double.parseDouble(tokens[6]),Double.parseDouble(tokens[7]), Integer.parseInt(tokens[8]));
                System.out.println(" new bus: " + Integer.toString(busID) + " created");
                break;
            case "add_train":
                int trainID = martaModel.makeTrain(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Integer.parseInt(tokens[8]));
                System.out.println(" new train: " + Integer.toString(trainID) + " created");
                break;
            case "add_depot":
                int uniqueID = Integer.parseInt(tokens[1].trim());
                String name = tokens[2].trim();
                int x_coord = Integer.parseInt(tokens[3].trim());
                int y_coord = Integer.parseInt(tokens[4].trim());

                martaModel.makeDepot(uniqueID, name, x_coord, y_coord);
            	break;
            case "extend_route":
                System.out.println(" stop: " + Integer.parseInt(tokens[2]) + " appended to route " + Integer.parseInt(tokens[1]));
                martaModel.appendStopToRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                break;
            // To be added after implementation of Station Class and appendStationToRoute() in TransitSystem class
            /*
            case "extend_train_route":
                System.out.println(" station: " + Integer.parseInt(tokens[2]) + " appended to route " + Integer.parseInt(tokens[1]));
                martaModel.appendStationToRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                break;
            */
            case "upload_real_data":
                uploadMARTAData();
                break;
            case "step_once":
            	simEngine.triggerNextEvent(martaModel);
                System.out.println(" queue activated for 1 event");
                break;
            case "step_multi":
                System.out.println(" queue activated for " + Integer.parseInt(tokens[1]) + " event(s)");
                for (int i = 0; i < Integer.parseInt(tokens[1]); i++) {
                	// display the number of events completed for a given frequency
                	if (tokens.length >= 3) {
                		if (i % Integer.parseInt(tokens[2]) == 0) { System.out.println("> " + Integer.toString(i) + " events completed"); }
                	}
                	
                	// execute the next event
                	simEngine.triggerNextEvent(martaModel);
                	
                	// pause after each event for a given number of seconds
                	if (tokens.length >= 4) {
                		try { Thread.sleep(Integer.parseInt(tokens[3]) * 1000); }
                			catch (InterruptedException e) { e.printStackTrace(); }
                	}
                	// regenerate the model display (Graphviz dot file) for a given frequency
                	if (tokens.length >= 5) {
                		if (i % Integer.parseInt(tokens[4]) == 0) { martaModel.displayModel();}
                	}
                }
                break;
            case "system_report":
                System.out.println(" system report - stops, buses and routes:");
                for (Stop singleStop: martaModel.getStops().values()) { singleStop.displayInternalStatus(); }
                for (Bus singleBus: martaModel.getBuses().values()) { singleBus.displayInternalStatus(); }
                for (BusRoute singleBusRoute: martaModel.getBusRoutes().values()) { singleBusRoute.displayInternalStatus(); }
                for (RailRoute singleRailRoute: martaModel.getRailRoutes().values()) { singleRailRoute.displayInternalStatus(); }
                break;
            case "display_model":
            	martaModel.displayModel();
            	break;
            case "quit":
                System.out.println(" stop the command loop");
                return true;
            case "path_delay":
            	//sets the delay on the specified bus path
            	//format: path_delay,<StartAt>,<Duration>,<Stop_A>,<Stop_B>,<DelayFactor>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tdelay factor: %f\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Double.valueOf(tokens[5]));
            	
            	PathKey busDelayPathKey = martaModel.getPathKey(martaModel.getStop(Integer.decode(tokens[3])), martaModel.getStop(Integer.decode(tokens[4])));
            	SetPathDelayEvent setBusDelayEvent = new SetPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), busDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", setBusDelayEvent.toJSON());
            	simEngine.add(setBusDelayEvent);
            	ClearPathDelayEvent clearBusDelayEvent = new ClearPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), busDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", clearBusDelayEvent.toJSON());
            	simEngine.add(clearBusDelayEvent);
                return true;
            case "train_path_delay":
            	//sets the delay on the specified train path
            	//format: path_delay,<StartAt>,<Duration>,<Station_A>,<Station_B>,<DelayFactor>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tdelay factor: %f\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Double.valueOf(tokens[5]));
            	
            	PathKey railDelayPathKey = martaModel.getPathKey(martaModel.getRailStation(Integer.decode(tokens[3])), martaModel.getRailStation(Integer.decode(tokens[4])));
            	SetPathDelayEvent setRailDelayEvent = new SetPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), railDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", setRailDelayEvent.toJSON());
            	simEngine.add(setRailDelayEvent);
            	ClearPathDelayEvent clearRailDelayEvent = new ClearPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), railDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", clearRailDelayEvent.toJSON());
            	simEngine.add(clearRailDelayEvent);
            	return true;
            case "speed_limit":
            	//sets the speed limit on the specified bus path
            	//format: speed_limit,<StartAt>,<Duration>,<Stop_A>,<Stop_B>,<TopSpeed>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tspeed limit: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Integer.valueOf(tokens[5]));
            	
            	PathKey busSpeedPathKey = martaModel.getPathKey(martaModel.getStop(Integer.decode(tokens[3])), martaModel.getStop(Integer.decode(tokens[4])));
            	SetSpeedLimitEvent setBusSpeedEvent = new SetSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), busSpeedPathKey, Integer.valueOf(tokens[5]));
            	System.out.printf("%s\n", setBusSpeedEvent.toJSON());
            	simEngine.add(setBusSpeedEvent);
            	ClearSpeedLimitEvent clearBusSpeedEvent = new ClearSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), busSpeedPathKey);
            	System.out.printf("%s\n", clearBusSpeedEvent.toJSON());
            	simEngine.add(clearBusSpeedEvent);
            	return true;
            case "train_speed_limit":
            	//sets the speed limit on the specified train path
            	//format: speed_limit,<StartAt>,<Duration>,<Station_A>,<Station_B>,<TopSpeed>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tspeed limit: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Integer.valueOf(tokens[5]));
            	
            	PathKey railSpeedPathKey = martaModel.getPathKey(martaModel.getRailStation(Integer.decode(tokens[3])), martaModel.getRailStation(Integer.decode(tokens[4])));
            	SetSpeedLimitEvent setRailSpeedEvent = new SetSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), railSpeedPathKey, Integer.valueOf(tokens[5]));
            	System.out.printf("%s\n", setRailSpeedEvent.toJSON());
            	simEngine.add(setRailSpeedEvent);
            	ClearSpeedLimitEvent clearRailSpeedEvent = new ClearSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), railSpeedPathKey);
            	System.out.printf("%s\n", clearRailSpeedEvent.toJSON());
            	simEngine.add(clearRailSpeedEvent);
            	return true;
            case "stop_down":
            	//sets the down time on the specified bus stop
            	//format: stop_down,<StartAt>,<Duration>,<StopID>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tstopID: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]));
            	
            	Stop outOfServiceStop = martaModel.getStop(Integer.decode(tokens[3]));
            	FacilityOutOfServiceEvent setStopOutOfServiceEvent = new FacilityOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceStop);
            	System.out.printf("%s\n", setStopOutOfServiceEvent.toJSON());
            	simEngine.add(setStopOutOfServiceEvent);
            	FacilityResumeServiceEvent clearStopOutOfServiceEvent = new FacilityResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), outOfServiceStop);
            	System.out.printf("%s\n", clearStopOutOfServiceEvent.toJSON());
            	simEngine.add(clearStopOutOfServiceEvent);
            	return true;
            case "station_down":
            	//sets the down time on the specified rail station
            	//format: station_down,<StartAt>,<Duration>,<StationID>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tstationID: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]));
            	
            	RailStation outOfServiceStation = martaModel.getRailStation(Integer.decode(tokens[3]));
            	FacilityOutOfServiceEvent setStationOutOfServiceEvent = new FacilityOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceStation);
            	System.out.printf("%s\n", setStationOutOfServiceEvent.toJSON());
            	simEngine.add(setStationOutOfServiceEvent);
            	FacilityResumeServiceEvent clearStationOutOfServiceEvent = new FacilityResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), outOfServiceStation);
            	System.out.printf("%s\n", clearStationOutOfServiceEvent.toJSON());
            	simEngine.add(clearStationOutOfServiceEvent);
            	return true;
            case "bus_down":
            	//sets the down time on the specified bus
            	//format: bus_down,<StartAt>,<BusID>,<TowingDuration>,<RepairDuration>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tbusID: %d\n\trepairDuration: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]));
            	
            	Bus outOfServiceBus = martaModel.getBus(Integer.decode(tokens[3]));
            	VehicleOutOfServiceEvent setBusOutOfServiceEvent = new VehicleOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceBus);
            	System.out.printf("%s\n", setBusOutOfServiceEvent.toJSON());
            	simEngine.add(setBusOutOfServiceEvent);
            	VehicleResumeServiceEvent clearBusOutOfServiceEvent = new VehicleResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2])+Integer.decode(tokens[4]), outOfServiceBus);
            	System.out.printf("%s\n", clearBusOutOfServiceEvent.toJSON());
            	simEngine.add(clearBusOutOfServiceEvent);
            	return true;
            case "train_down":
            	//sets the down time on the specified train
            	//format: train_down,<StartAt>,<TrainID><StallDuration>,<RepairDuration>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\trailCarID: %d\n\trepairDuration: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]));
            	
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tbusID: %d\n\trepairDuration: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]));
            	
            	//ToDo: Update maybe needed to account for stalled train in station
            	RailCar outOfServiceRailCar= martaModel.getTrain(Integer.decode(tokens[3]));
            	VehicleOutOfServiceEvent setRailOutOfServiceEvent = new VehicleOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceRailCar);
            	System.out.printf("%s\n", setRailOutOfServiceEvent.toJSON());
            	simEngine.add(setRailOutOfServiceEvent);
            	VehicleResumeServiceEvent clearRailOutOfServiceEvent = new VehicleResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2])+Integer.decode(tokens[4]), outOfServiceRailCar);
            	System.out.printf("%s\n", clearRailOutOfServiceEvent.toJSON());
            	simEngine.add(clearRailOutOfServiceEvent);
            	return true;
            default:
                System.out.println(" command not recognized");
                return true;
        }
        return false;
    	}
    }

    public void runInterpreter() {
        Scanner takeCommand = new Scanner(System.in);
        boolean done = false;
        do {
            System.out.print("# main: ");
            String userCommandLine = takeCommand.nextLine();
            done = processCommand(userCommandLine);

        } while (!done);

        takeCommand.close();
    }

    private static void uploadMARTAData() {
        ResultSet rs;
        int recordCounter;

        Integer stopID, routeID;
        String stopName, routeName;
        // String direction;
        Double latitude, longitude;

        // intermediate data structures needed for assembling the routes
        HashMap<Integer, ArrayList<Integer>> routeLists = new HashMap<Integer, ArrayList<Integer>>();
        ArrayList<Integer> targetList;
        ArrayList<Integer> circularRouteList = new ArrayList<Integer>();

        try {
    		// connect to the local database system
        	System.out.println(" connecting to the database");
    		String url = "jdbc:postgresql://localhost:5432/martadb";
    		Properties props = new Properties();
    		props.setProperty("user", "postgres");
    		props.setProperty("password", "cs6310");
    		String key;
    		
    		key="user";
    		if(FileProps.contains(key)) {
    			props.setProperty(key, FileProps.get(key));
    		}
    		key="password";
    		if(FileProps.contains(key)) {
    			props.setProperty(key, FileProps.get(key));
    		}
    		key="ssl";
    		if(FileProps.contains(key)) {
    			props.setProperty(key, FileProps.get(key));
    		}
    		key="url";
    		if(FileProps.contains(key)) {
    			url= FileProps.get(key);
    		}
    		

			Connection conn = DriverManager.getConnection(url, props);
			Statement stmt = conn.createStatement();

			// create the stops
        	System.out.print(" extracting and adding the stops: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_stops");
            while (rs.next()) {
                stopID = rs.getInt("min_stop_id");
                stopName = rs.getString("stop_name");
                latitude = rs.getDouble("latitude");
                longitude = rs.getDouble("longitude");

                martaModel.makeStop(stopID,stopName,0,latitude,longitude);
                recordCounter++;
            }
            System.out.println(Integer.toString(recordCounter) + " added");

            // create the bus routes
        	System.out.print(" extracting and adding the routes: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_routes");
            while (rs.next()) {
                routeID = rs.getInt("route");
                routeName = rs.getString("route_name");

                martaModel.makeBusRoute(routeID, routeID, routeName);
                recordCounter++;

                // initialize the list of stops for the route as needed
                routeLists.putIfAbsent(routeID, new ArrayList<Integer>());
            }
            System.out.println(Integer.toString(recordCounter) + " added");
            
            // create the rail routes
        	System.out.print(" extracting and adding the routes: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_railroutes"); //rail route table apcdata_railroutes
            while (rs.next()) {
                routeID = rs.getInt("route");
                routeName = rs.getString("route_name");

                martaModel.makeRailRoute(routeID, routeID, routeName);
                recordCounter++;

                // initialize the list of stops for the route as needed
                routeLists.putIfAbsent(routeID, new ArrayList<Integer>());
            }
            System.out.println(Integer.toString(recordCounter) + " added");

            // add the stops to all of the routes
        	System.out.print(" extracting and assigning stops to the routes: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_routelist_oneway");
            while (rs.next()) {
                routeID = rs.getInt("route");
                stopID = rs.getInt("min_stop_id");
                // direction = rs.getString("direction");

                targetList = routeLists.get(routeID);
                if (!targetList.contains(stopID)) {
                    martaModel.appendStopToRoute(routeID, stopID);
                    recordCounter++;
                    targetList.add(stopID);
                    // if (direction.equals("Clockwise")) { circularRouteList.add(routeID); }
                }
            }

            // add the reverse "route back home" stops for two-way routes
            for (Integer reverseRouteID : routeLists.keySet()) {
                if (!circularRouteList.contains(reverseRouteID)) {
                    targetList = routeLists.get(reverseRouteID);
                    for (int i = targetList.size() - 1; i > 0; i--) {
                        martaModel.appendStopToRoute(reverseRouteID, targetList.get(i));
                    }
                }
            }
            System.out.println(Integer.toString(recordCounter) + " assigned");
            
            // To be added after implementation of Station Class and appendStationToRoute() in TransitSystem class
            /*
            // add the stations to all of the routes
        	System.out.print(" extracting and assigning stations to the routes: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_railroutelist_oneway");
            while (rs.next()) {
                routeID = rs.getInt("route");
                stationID = rs.getInt("min_station_id");
                // direction = rs.getString("direction");

                targetList = railrouteLists.get(routeID);
                if (!targetList.contains(stationID)) {
                    martaModel.appendStationToRoute(routeID, stationID);
                    recordCounter++;
                    targetList.add(stationID);
                    // if (direction.equals("Clockwise")) { circularRouteList.add(routeID); }
                }
            }

            // add the reverse "route back home" stations for two-way routes
            for (Integer reverseRouteID : railrouteLists.keySet()) {
                if (!circularRouteList.contains(reverseRouteID)) {
                    targetList = railrouteLists.get(reverseRouteID);
                    for (int i = targetList.size() - 1; i > 0; i--) {
                        martaModel.appendStationToRoute(reverseRouteID, targetList.get(i));
                    }
                }
            }
            System.out.println(Integer.toString(recordCounter) + " assigned");
            */

            // create the buses and related event(s)
        	System.out.print(" extracting and adding the buses and events: ");
        	recordCounter = 0;
            int busID = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_bus_distributions");
            while (rs.next()) {
                routeID = rs.getInt("route");
                int minBuses = rs.getInt("min_buses");
                int avgBuses  = rs.getInt("avg_buses");
                int maxBuses = rs.getInt("max_buses");

                int routeLength = martaModel.getBusRoute(routeID).getLength();
                int suggestedBuses = randomBiasedValue(minBuses, avgBuses, maxBuses);
                int busesOnRoute = Math.max(1, Math.min(routeLength / 2, suggestedBuses));

                int startingPosition = 0;
                int skip = Math.max(1, routeLength / busesOnRoute);
                for (int i = 0; i < busesOnRoute; i++) {
                    martaModel.makeBus(busID, routeID, startingPosition + i * skip, 0, 10, 100, 100,60);
                    //simEngine.addNewEvent(0,"move_bus", busID++);
                    recordCounter++;
                }
            }
            System.out.println(Integer.toString(recordCounter) + " added");

            // create the rider-passenger generator and associated event(s)
        	System.out.print(" extracting and adding the rider frequency timeslots: ");
        	recordCounter = 0;
            rs = stmt.executeQuery("SELECT * FROM apcdata_rider_distributions");
            while (rs.next()) {
                stopID = rs.getInt("min_stop_id");
                int timeSlot = rs.getInt("time_slot");
                int minOns = rs.getInt("min_ons");
                int avgOns  = rs.getInt("avg_ons");
                int maxOns = rs.getInt("max_ons");
                int minOffs = rs.getInt("min_offs");
                int avgOffs = rs.getInt("avg_offs");
                int maxOffs = rs.getInt("max_offs");

                martaModel.getStop(stopID).addArrivalInfo(timeSlot, minOns, avgOns, maxOns, minOffs, avgOffs, maxOffs);
                recordCounter++;
            }
            System.out.println(Integer.toString(recordCounter) + " added");


        } catch (Exception e) {
            System.err.println("Discovered exception: ");
            System.err.println(e.getMessage());
        }
    }

    private static int randomBiasedValue(int lower, int middle, int upper) {
        int lowerRange = randGenerator.nextInt(middle - lower + 1) + lower;
        int upperRange = randGenerator.nextInt(upper - middle + 1) + middle;
        return (lowerRange + upperRange) /2;
    }
    
    public String toJSON() {
    	synchronized(lock) {
    	StringBuilder sb = new StringBuilder();
    	sb.append('{');
    	sb.append("\"time\":");
    	sb.append(simEngine.getTime());
    	sb.append(',');
    	sb.append("\"system\":");
       	sb.append(martaModel.toJSON());
       	if(simEngine.hasEvents()) {
       		sb.append(",\"events\":[");
       		boolean isFirst = true;
       		for(SimEvent event : simEngine.getEvents()) {
        		if(isFirst) {
        			isFirst = !isFirst;
        		}
        		else {
        			sb.append(',');
        		}
        		sb.append(event.toJSON());
       		}
       		sb.append(']');
       	}
        sb.append('}');
    	return sb.toString();
    	}
    }

	@Override
	public void updateState() {
    	synchronized(lock) {
		if(updateManager==null) return;
		String message = toJSON();
		System.out.printf("new state:\n---------------------\n%s\n---------------------\n", message);
		try {
			updateManager.post(message);
		} catch (IOException e) {
			System.out.printf("Some error with posting message to socket");
			e.printStackTrace();
		}
    	}
	}

	public void setUpdateManager(UpdateManager updateManager) {
		this.updateManager= updateManager;
	}
}
