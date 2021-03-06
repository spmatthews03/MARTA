package edu.gatech;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import group_a7_8.FileProps;
import group_a7_8.FuelConsumption;
import group_a7_8.Hazard;
import group_a7_8.Path;
import group_a7_8.PathKey;
import group_a7_8.RouteDefinition;
import group_a7_8.event.ClearPathDelayEvent;
import group_a7_8.event.ClearSpeedLimitEvent;
import group_a7_8.event.FacilityOutOfServiceEvent;
import group_a7_8.event.FacilityResumeServiceEvent;
import group_a7_8.event.MoveBusEvent;
import group_a7_8.event.MoveTrainEvent;
import group_a7_8.event.SetPathDelayEvent;
import group_a7_8.event.SetSpeedLimitEvent;
import group_a7_8.event.SimEvent;
import group_a7_8.event.VehicleOutOfServiceEvent;
import group_a7_8.event.VehicleResumeServiceEvent;
import group_a7_8.persistence.BusDAO;
import group_a7_8.persistence.BusRouteDAO;
import group_a7_8.persistence.BusStopDAO;
import group_a7_8.persistence.DAOManager;
import group_a7_8.persistence.GenericDAO;
import group_a7_8.persistence.HazardDAO;
import group_a7_8.persistence.PathDAO;
import group_a7_8.persistence.RailCarDAO;
import group_a7_8.persistence.RailRouteDAO;
import group_a7_8.persistence.RailStationDAO;
import group_a7_8.persistence.RouteDefinitionDAO;
import group_a7_8.persistence.SetPathDelayEventDAO;
import group_a7_8.persistence.DAOManager.Table;
import group_a7_8.persistence.DepotDAO;
import group_a7_8.persistence.FuelConsumptionDAO;
import group_a7_8.server.StateChangeListener;
import group_a7_8.server.UpdateManager;
import group_a7_8.PathKey;

public class SimDriver implements StateChangeListener{
	private final static Object lock = new Object();
    private static SimQueue simEngine;
    private static TransitSystem martaModel;
    private static Random randGenerator;
    private Hashtable<Table, GenericDAO> DAOs = new Hashtable<Table, GenericDAO>();
	private boolean persistenceOn;
	private boolean hasPriorSim = false;
	public boolean hasPriorSim() {return hasPriorSim;}
    
	public SimDriver() {
		this(false);
	}
	
    public SimDriver(boolean persistenceOn) {
        simEngine = new SimQueue();
        simEngine.setStateChangeListener(this);
        martaModel = new TransitSystem();
        martaModel.setStateChangeListener(this);
        randGenerator = new Random();
        this.persistenceOn = persistenceOn;
    }
    
    public GenericDAO getDao(Table table) throws ClassNotFoundException, SQLException, Exception {
    	if (!DAOs.containsKey(table)) {
    		DAOs.put(table, DAOManager.getInstance(martaModel,simEngine).getDAO(table));
    	}
    	return DAOs.get(table);
    }
    
    private final String DELIMITER = ",";
	    
	private UpdateManager updateManager;
    public boolean processCommand(String userCommandLine){
    	synchronized(lock) {
    	System.out.printf("SimDriver: `%s` cmd processing\n", userCommandLine);

        String[] tokens;
        tokens = userCommandLine.split(DELIMITER);

        //System.out.println(tokens[0]);

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
	                    //MoveBusEvent event = new MoveBusEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), bus);
	                    MoveBusEvent event = new MoveBusEvent(martaModel, Integer.parseInt(tokens[3]), Integer.decode(tokens[1]), bus);
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
	                    //MoveTrainEvent event = new MoveTrainEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), train);
	                    MoveTrainEvent event = new MoveTrainEvent(martaModel, Integer.parseInt(tokens[3]), Integer.decode(tokens[1]), train);
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
            	int station_uniqueID = Integer.parseInt(tokens[1].trim()); 
            	String inputName 	 = tokens[2].trim();
            	int inputRiders 	 = Integer.parseInt(tokens[3].trim());
            	double inputXCoord	 = Double.parseDouble(tokens[4].trim());
            	double inputYCoord	 = Double.parseDouble(tokens[5].trim());
            	
            	int railStationID = martaModel.makeRailStation(station_uniqueID, inputName, inputRiders, inputXCoord, inputYCoord);
                System.out.println(" new rail station: " + Integer.toString(railStationID) + " created");
                break;
            case "add_route":
                int busRouteID = martaModel.makeBusRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), tokens[3]);
                System.out.println(" new bus route: " + Integer.toString(busRouteID) + " created");
                break;
            case "add_train_route":
            	int train_route_uniqueID = Integer.parseInt(tokens[1].trim());
            	int train_route_number = Integer.parseInt(tokens[2].trim());
            	String train_route_inputName = tokens[3].trim();
            	
            	int railRouteID = martaModel.makeRailRoute(train_route_uniqueID, train_route_number, train_route_inputName);
                System.out.println(" new rail route: " + Integer.toString(railRouteID) + " created");
                break;
            case "add_bus":
                int busID = martaModel.makeBus(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]),
                        Integer.parseInt(tokens[3]), Integer.parseInt(tokens[4]), Integer.parseInt(tokens[5]), Double.parseDouble(tokens[6]),Double.parseDouble(tokens[7]), Integer.parseInt(tokens[8]));
                System.out.println(" new bus: " + Integer.toString(busID) + " created");
                break;
            case "add_train":
            	int train_uniqueID = Integer.parseInt(tokens[1].trim());
            	int train_inputRoute = Integer.parseInt(tokens[2].trim());
            	int train_inputLocation = Integer.parseInt(tokens[3].trim());
            	int train_inputPassengers = Integer.parseInt(tokens[4].trim());
            	int train_inputCapacity = Integer.parseInt(tokens[5].trim());
            	int train_inputSpeed = Integer.parseInt(tokens[6].trim());
            	
                int trainID = martaModel.makeTrain(train_uniqueID, train_inputRoute,
                								   train_inputLocation, train_inputPassengers,
                								   train_inputCapacity, train_inputSpeed);
                //System.out.println(" new train: " + Integer.toString(trainID) + " created");
                break;
            case "add_depot":
            	int uniqueID = Integer.parseInt(tokens[1].trim());
                String name = tokens[2].trim();
                double x_coord = Double.parseDouble(tokens[3].trim());
                double y_coord = Double.parseDouble(tokens[4].trim());

                martaModel.makeDepot(uniqueID, name, x_coord, y_coord);
            	break;
            case "extend_route":
                System.out.println(" stop: " + Integer.parseInt(tokens[2]) + " appended to route " + Integer.parseInt(tokens[1]));
            	
            	if (martaModel.getBusStop(Integer.parseInt(tokens[2])) == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[2]) + " has not been created");
                    return true;
            	}
            	
            	if (martaModel.getBusRoute(Integer.parseInt(tokens[1])) == null){
            		System.out.println(" bus route " + Integer.parseInt(tokens[1]) + " has not been created");
                    return true;
            	}
            	
                martaModel.appendStopToRoute(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                break;

            case "extend_train_route":
                int ext_train_route_railStationID = Integer.parseInt(tokens[2].trim());
                int ext_train_route_routeID = Integer.parseInt(tokens[1].trim());

            	System.out.println(" station: " + ext_train_route_railStationID + " appended to route " + ext_train_route_routeID);
                
            	if (martaModel.getRailStation(ext_train_route_railStationID) == null){
            		System.out.println(" station " + ext_train_route_railStationID + " has not been created");
                    return true;
            	}
            	
            	if (martaModel.getRailRoute(ext_train_route_routeID) == null){
            		System.out.println(" rail route " + ext_train_route_routeID + " has not been created");
                    return true;
            	}
            	
                martaModel.appendStationToRoute(ext_train_route_routeID, ext_train_route_railStationID);
                break;
            // Remove according to instructions
            /*case "upload_real_data":
                uploadMARTAData();
                break;*/
            case "step_once":
            	simEngine.triggerNextEvent(martaModel);
                //System.out.println(" queue activated for 1 event");
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
                for (BusStop singleStop: martaModel.getStops().values()) { singleStop.displayInternalStatus(); }
                for (Bus singleBus: martaModel.getBuses().values()) { singleBus.displayInternalStatus(); }
                for (BusRoute singleBusRoute: martaModel.getBusRoutes().values()) { singleBusRoute.displayInternalStatus(); }
                for (RailStation singleStation: martaModel.getRailStations().values()) { singleStation.displayInternalStatus(); }
                for (RailCar singleTrain: martaModel.getTrains().values()) { singleTrain.displayInternalStatus(); }
                for (RailRoute singleRailRoute: martaModel.getRailRoutes().values()) { singleRailRoute.displayInternalStatus(); }
                break;
            case "display_model":
            	martaModel.displayModel();
            	break;
            case "quit":
				try {
					this.save();
				} catch (Exception e) {
					System.out.printf("ERROR: Unable to save state due to error: %s\n",e.getMessage());
					e.printStackTrace();
				} 
				martaModel.reset();
				simEngine.reset();
				updateState();

            	return true;
            case "path_delay":
            	//sets the delay on the specified bus path
            	//format: path_delay,<StartAt>,<Duration>,<Stop_A>,<Stop_B>,<DelayFactor>         	            	
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\torigin: %d\n\tdestination: %d\n\tdelay factor: %f\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Double.valueOf(tokens[5]));
            	
            	BusStop path_delay_origin_bus = martaModel.getBusStop(Integer.parseInt(tokens[3]));
            	BusStop path_delay_destin_bus = martaModel.getBusStop(Integer.parseInt(tokens[4]));
            	
            	if (path_delay_origin_bus == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	} else if (path_delay_destin_bus == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[4]) + " has not been created");
                    return false;
            	} else if (martaModel.getPathKey(path_delay_origin_bus, path_delay_destin_bus) == null){
            		System.out.println(" There is no path between stop " + Integer.parseInt(tokens[3]) + " and stop " + Integer.parseInt(tokens[4]));
                    return false;
            	}
            	            	
            	PathKey busDelayPathKey = martaModel.getPathKey(martaModel.getBusStop(Integer.decode(tokens[3])), martaModel.getBusStop(Integer.decode(tokens[4])));
            	SetPathDelayEvent setBusDelayEvent = new SetPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), busDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", setBusDelayEvent.toJSON());
            	simEngine.add(setBusDelayEvent);
            	ClearPathDelayEvent clearBusDelayEvent = new ClearPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), busDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", clearBusDelayEvent.toJSON());
            	simEngine.add(clearBusDelayEvent);
            	break;
            case "train_path_delay":
            	//sets the delay on the specified train path
            	//format: path_delay,<StartAt>,<Duration>,<Station_A>,<Station_B>,<DelayFactor>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tdelay factor: %f\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Double.valueOf(tokens[5]));
            	
            	RailStation path_delay_origin_train = martaModel.getRailStation(Integer.parseInt(tokens[3]));
            	RailStation path_delay_destin_train = martaModel.getRailStation(Integer.parseInt(tokens[4]));
            	           	
            	if (path_delay_origin_train == null){
            		System.out.println(" station " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	} else if (path_delay_destin_train == null){
            		System.out.println(" station " + Integer.parseInt(tokens[4]) + " has not been created");
                    return false;
            	} else if (martaModel.getPathKey(path_delay_origin_train, path_delay_destin_train) == null){
            		System.out.println(" There is no path between station " + Integer.parseInt(tokens[3]) + " and station " + Integer.parseInt(tokens[4]));
                    return false;
            	}
            	
            	PathKey railDelayPathKey = martaModel.getPathKey(martaModel.getRailStation(Integer.decode(tokens[3])), martaModel.getRailStation(Integer.decode(tokens[4])));
            	SetPathDelayEvent setRailDelayEvent = new SetPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), railDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", setRailDelayEvent.toJSON());
            	simEngine.add(setRailDelayEvent);
            	ClearPathDelayEvent clearRailDelayEvent = new ClearPathDelayEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), railDelayPathKey, Double.valueOf(tokens[5]));
            	System.out.printf("%s\n", clearRailDelayEvent.toJSON());
            	simEngine.add(clearRailDelayEvent);
            	break;
            case "speed_limit":
            	//sets the speed limit on the specified bus path
            	//format: speed_limit,<StartAt>,<Duration>,<Stop_A>,<Stop_B>,<TopSpeed>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\torigin: %d\n\tdestination: %d\n\tspeed limit: %d\n",
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Integer.valueOf(tokens[5]));
            	
            	BusStop speed_limit_origin_bus = martaModel.getBusStop(Integer.parseInt(tokens[3]));
            	BusStop speed_limit_destin_bus = martaModel.getBusStop(Integer.parseInt(tokens[4]));
            	           	
            	if (speed_limit_origin_bus == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	} else if (speed_limit_destin_bus == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[4]) + " has not been created");
                    return false;
            	} else if (martaModel.getPathKey(speed_limit_origin_bus, speed_limit_destin_bus) == null){
            		System.out.println(" There is no path between stop " + Integer.parseInt(tokens[3]) + " and stop " + Integer.parseInt(tokens[4]));
                    return false;
            	}
            	
            	PathKey busSpeedPathKey = martaModel.getPathKey(martaModel.getBusStop(Integer.decode(tokens[3])), martaModel.getBusStop(Integer.decode(tokens[4])));
            	SetSpeedLimitEvent setBusSpeedEvent = new SetSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), busSpeedPathKey, Integer.valueOf(tokens[5]));
            	System.out.printf("%s\n", setBusSpeedEvent.toJSON());
            	simEngine.add(setBusSpeedEvent);
            	ClearSpeedLimitEvent clearBusSpeedEvent = new ClearSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), busSpeedPathKey);
            	System.out.printf("%s\n", clearBusSpeedEvent.toJSON());
            	simEngine.add(clearBusSpeedEvent);
            	break;
            case "train_speed_limit":
            	//sets the speed limit on the specified train path
            	//format: speed_limit,<StartAt>,<Duration>,<Station_A>,<Station_B>,<TopSpeed>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\totigin: %d\n\tdestination: %d\n\tspeed limit: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]),Integer.decode(tokens[4]),Integer.valueOf(tokens[5]));
            	
            	RailStation speed_limit_origin_train = martaModel.getRailStation(Integer.parseInt(tokens[3]));
            	RailStation speed_limit_destin_train = martaModel.getRailStation(Integer.parseInt(tokens[4]));
            	           	
            	if (speed_limit_origin_train == null){
            		System.out.println(" station " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	} else if (speed_limit_destin_train == null){
            		System.out.println(" station " + Integer.parseInt(tokens[4]) + " has not been created");
                    return false;
            	} else if (martaModel.getPathKey(speed_limit_origin_train, speed_limit_destin_train) == null){
            		System.out.println(" There is no path between station " + Integer.parseInt(tokens[3]) + " and station " + Integer.parseInt(tokens[4]));
                    return false;
            	}
            	            	
            	PathKey railSpeedPathKey = martaModel.getPathKey(martaModel.getRailStation(Integer.decode(tokens[3])), martaModel.getRailStation(Integer.decode(tokens[4])));
            	SetSpeedLimitEvent setRailSpeedEvent = new SetSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), railSpeedPathKey, Integer.valueOf(tokens[5]));
            	System.out.printf("%s\n", setRailSpeedEvent.toJSON());
            	simEngine.add(setRailSpeedEvent);
            	ClearSpeedLimitEvent clearRailSpeedEvent = new ClearSpeedLimitEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), railSpeedPathKey);
            	System.out.printf("%s\n", clearRailSpeedEvent.toJSON());
            	simEngine.add(clearRailSpeedEvent);
            	break;
            case "stop_down":
            	//sets the down time on the specified bus stop
            	//format: stop_down,<StartAt>,<Duration>,<StopID>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tstopID: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]));
            	
            	if (martaModel.getBusStop(Integer.parseInt(tokens[3])) == null){
            		System.out.println(" stop " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	}
            	
            	BusStop outOfServiceStop = martaModel.getBusStop(Integer.decode(tokens[3]));
            	FacilityOutOfServiceEvent setStopOutOfServiceEvent = new FacilityOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceStop);
            	System.out.printf("%s\n", setStopOutOfServiceEvent.toJSON());
            	simEngine.add(setStopOutOfServiceEvent);
            	FacilityResumeServiceEvent clearStopOutOfServiceEvent = new FacilityResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), outOfServiceStop);
            	System.out.printf("%s\n", clearStopOutOfServiceEvent.toJSON());
            	simEngine.add(clearStopOutOfServiceEvent);
            	break;
            case "station_down":
            	//sets the down time on the specified rail station
            	//format: station_down,<StartAt>,<Duration>,<StationID>
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tstationID: %d\n", 
            			tokens[0],Integer.decode(tokens[1]),Integer.decode(tokens[2]),Integer.decode(tokens[3]));
            	
            	if (martaModel.getRailStation(Integer.parseInt(tokens[3])) == null){
            		System.out.println(" station " + Integer.parseInt(tokens[3]) + " has not been created");
                    return false;
            	}
            	
            	RailStation outOfServiceStation = martaModel.getRailStation(Integer.decode(tokens[3]));
            	FacilityOutOfServiceEvent setStationOutOfServiceEvent = new FacilityOutOfServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1]), outOfServiceStation);
            	System.out.printf("%s\n", setStationOutOfServiceEvent.toJSON());
            	simEngine.add(setStationOutOfServiceEvent);
            	

            	FacilityResumeServiceEvent clearStationOutOfServiceEvent = new FacilityResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2]), outOfServiceStation);
            	System.out.printf("%s\n", clearStationOutOfServiceEvent.toJSON());
            	simEngine.add(clearStationOutOfServiceEvent);
            	
            	break;
            case "bus_down":
            	//sets the down time on the specified bus
            	//format: bus_down,<StartAt>,<BusID>,<TowingDuration>,<RepairDuration>
            	String  bus_down_cmd = tokens[0].trim();
            	Integer StartAt = Integer.decode(tokens[1].trim());
            	Integer BusID = Integer.decode(tokens[2].trim());
            	Integer TowingDuration = Integer.decode(tokens[3].trim());
            	Integer RepairDuration = Integer.decode(tokens[4].trim());
            	
            	System.out.printf("%s:\n\tstart at: %d\n\tduration: %d\n\tbusID: %d\n\trepairDuration: %d\n", 
            			bus_down_cmd,StartAt,BusID,TowingDuration,RepairDuration);
            	
            	if (martaModel.getBus(Integer.parseInt(tokens[2])) == null){
            		System.out.println(" bus " + BusID + " has not been created");
                    return false;
            	}
            	
            	Bus outOfServiceBus = martaModel.getBus(BusID);
            	VehicleOutOfServiceEvent setBusOutOfServiceEvent = new VehicleOutOfServiceEvent(martaModel, simEngine.getNextEventID(), StartAt, outOfServiceBus, TowingDuration, RepairDuration);
            	System.out.printf("%s\n", setBusOutOfServiceEvent.toJSON());
            	simEngine.add(setBusOutOfServiceEvent);
            	//VehicleResumeServiceEvent clearBusOutOfServiceEvent = new VehicleResumeServiceEvent(martaModel, simEngine.getNextEventID(), Integer.decode(tokens[1])+Integer.decode(tokens[2])+Integer.decode(tokens[4]), outOfServiceBus);
            	//System.out.printf("%s\n", clearBusOutOfServiceEvent.toJSON());
            	//simEngine.add(clearBusOutOfServiceEvent);
            	break;
            case "train_down":
            	//sets the down time on the specified train
            	//format: train_down,<StartAt>,<TrainID><StallDuration>,<RepairDuration>
            	String  train_down_cmd 			= tokens[0].trim();
            	Integer start_time 				= Integer.decode(tokens[1].trim());
            	Integer my_trainID  			= Integer.decode(tokens[2].trim());
            	Integer delta_stall_period		= Integer.decode(tokens[3].trim());
            	Integer repairDuration          = Integer.decode(tokens[4].trim());
         	
            	RailCar tran_broken_down = martaModel.getTrain(my_trainID);
            	if (tran_broken_down == null) {
            		System.out.println("Error: train " + my_trainID + " has not been created");
                    return false;
            	}
            	Path path_next = tran_broken_down.get_path_next();
            	path_next.set_delta_stall_duration(delta_stall_period);
            	VehicleOutOfServiceEvent setRailOutOfServiceEvent =
            			new VehicleOutOfServiceEvent(martaModel, simEngine.getNextEventID(),
            										 start_time, tran_broken_down,
            										 delta_stall_period, repairDuration);

            	//System.out.printf("SimDriver: %s\n", setRailOutOfServiceEvent.toJSON());
            	simEngine.add(setRailOutOfServiceEvent);

            	break;
            case "fuel_report":
            	
            	Hashtable<Integer, Bus> buses = martaModel.getBuses();
            	List<Integer> bus_IDs = new ArrayList(buses.keySet());
            	Collections.sort(bus_IDs);
            	for (Integer bus_ID : bus_IDs) {
            		Bus bus = martaModel.getBus(bus_ID);
            		double fuel_level = bus.getFuelLevel();
            		double total_fuel_consumed = martaModel.getTotalFuelConsumed(bus);
            		System.out.printf(" bus %d - current fuel level: %f, total fuel consumed: %f\n", bus_ID, fuel_level, total_fuel_consumed);
            	}
            	            	
                break;

            default:
                System.out.println(" command not recognized");
                return false;
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

            int comment_position = userCommandLine.indexOf("#");
            if (comment_position != -1) {
            	userCommandLine = userCommandLine.substring(0, comment_position);
            }
            done = processCommand(userCommandLine);

        } while (!done);

        takeCommand.close();
    }
    /*
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

                martaModel.getBusStop(stopID).addArrivalInfo(timeSlot, minOns, avgOns, maxOns, minOffs, avgOffs, maxOffs);
                recordCounter++;
            }
            System.out.println(Integer.toString(recordCounter) + " added");


        } catch (Exception e) {
            System.err.println("Discovered exception: ");
            System.err.println(e.getMessage());
        }
    }
    */
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
       	if(martaModel.getFuelConsumption().size()>0) {
			sb.append(",\"reports\":[");
			boolean firstVehicle=true;
			for(Vehicle vehicle : martaModel.getFuelConsumption().keySet()) {			
				if(firstVehicle) {
					firstVehicle = false;
				}
				else{
					sb.append(",");
				}
				sb.append("{\"vehicle\":");
				sb.append(vehicle.toJSON());
				ArrayList<FuelConsumption> busConsumption = martaModel.getFuelConsumption().get(vehicle);
				if(busConsumption!=null && busConsumption.size()>0) {
					sb.append(",\"reports\":[");
					boolean isFirst = true;
					for(FuelConsumption report : busConsumption) {
						if(isFirst) {
							isFirst = false;
						}
						else{
							sb.append(",");
						}
						sb.append(report.toJSON());
					}
					sb.append("]");
				}
				sb.append("}");
			}
			sb.append("]");
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
		//System.out.printf("new state:\n---------------------\n%s\n---------------------\n", message);
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

	public int getTime() {return simEngine.getTime();}
	
	public String serializeFuelConsumptionToJSON() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"time\":");
		sb.append(getTime());
		if(martaModel.getFuelConsumption().size()>0) {
			sb.append(",\"reports\":[");
			boolean firstVehicle=true;
			for(Vehicle vehicle : martaModel.getFuelConsumption().keySet()) {			
				if(firstVehicle) {
					firstVehicle = false;
				}
				else{
					sb.append(",");
				}
				sb.append("{\"vehicle\":");
				sb.append(vehicle.toJSON());
				ArrayList<FuelConsumption> busConsumption = martaModel.getFuelConsumption().get(vehicle);
				if(busConsumption!=null && busConsumption.size()>0) {
					sb.append(",\"reports\":[");
					boolean isFirst = true;
					for(FuelConsumption report : busConsumption) {
						if(isFirst) {
							isFirst = false;
						}
						else{
							sb.append(",");
						}
						sb.append(report.toJSON());
					}
					sb.append("]");
				}
				sb.append("}");
			}
			sb.append("]");
		}
		sb.append("}");
		return sb.toString();
	}

	public void reset() {
		//clear the hashtables
		martaModel.reset();
		simEngine.reset();
		//clear the database
		for(Table t : Table.values()) {
			GenericDAO dao;
			try {
				dao = DAOManager.getInstance(martaModel,simEngine).getDAO(t);
				dao.clear();
			} catch (Exception e) {
				System.out.printf("unable to clear the database due to error %s\n",e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasSavedSimulation() throws ClassNotFoundException, SQLException, Exception {
		int c=0;
		for(Table t : Table.values()) {
				c+=getDao(t).count();
		}
		return c>0;
	}

	@SuppressWarnings("unchecked")
	public boolean save() throws ClassNotFoundException, SQLException, Exception {
        if(persistenceOn) {
        	System.out.println("saving state ...");
        	((DepotDAO)getDao(Table.DEPOT)).save(martaModel.getDepot());
        for (Bus bus : martaModel.getBuses().values()) {
    		try {
				((BusDAO)getDao(Table.BUS)).save(bus);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save bus");
			}	
    	}
    	for (BusRoute busRoute : martaModel.getBusRoutes().values()) {
    		try {
				((BusRouteDAO)getDao(Table.BUSROUTE)).save(busRoute);
				for(int stopLocation=0;stopLocation<busRoute.getLength();stopLocation++){
	    			System.out.println("route "+busRoute.getName()+" has "+busRoute.getLength()+" locations");
	    			Facility facility = busRoute.getBusStop(martaModel, stopLocation);
	    			RouteDefinition rdef = new RouteDefinition(busRoute, stopLocation, facility);
	    			((RouteDefinitionDAO)getDao(Table.ROUTEDEFINITION)).save(rdef);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save bus route");
			}	
    	}
    	for (BusStop busStop : martaModel.getStops().values()) {
    		try {
				((BusStopDAO)getDao(Table.BUSSTOP)).save(busStop);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save bus stop");
			}	
    	}
    	for (RailCar railCar : martaModel.getTrains().values()) {
    		try {
				((RailCarDAO)getDao(Table.RAILCAR)).save(railCar);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save train");
			}	
    	}
    	for (RailRoute railRoute : martaModel.getRailRoutes().values()) {
    		try {
				((RailRouteDAO)getDao(Table.RAILROUTE)).save(railRoute);
				for(int stopLocation=0;stopLocation<railRoute.getLength();stopLocation++){
	    			System.out.println("route "+railRoute.getName()+" has "+railRoute.getLength()+" locations");
	    			Facility facility = railRoute.getRailStation(martaModel, stopLocation);
	    			RouteDefinition rdef = new RouteDefinition(railRoute, stopLocation, facility);
	    			((RouteDefinitionDAO)getDao(Table.ROUTEDEFINITION)).save(rdef);
	    		}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save rail route");
			}	
    	}
    	for (RailStation railStation : martaModel.getRailStations().values()) {
    		try {
				((RailStationDAO)getDao(Table.RAILSTATION)).save(railStation);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save rail station");
			}	
    	}
    	
    	Enumeration<PathKey> pkEnum = martaModel.getPaths().keys();
    	while(pkEnum.hasMoreElements())
    	{
    		PathKey pk = pkEnum.nextElement();
    		if(martaModel.getPaths().containsKey(pk)) {
    			Path path = martaModel.getPaths().get(pk);
        		try {
    				((PathDAO)getDao(Table.PATH)).save(path);
    			} catch (Exception e) {
    				e.printStackTrace();
    				System.out.println("Unable to save path");
    			}	
    		}
    	}
    	
    	Enumeration<PathKey> pkEnum2 = martaModel.getAllHazards().keys();
    	while(pkEnum2.hasMoreElements())
    	{
    		PathKey pk = pkEnum2.nextElement();
    		ArrayList<Hazard> pathHazards = martaModel.getHazards(pk);
    		try {
    			for (int j = 0; j < pathHazards.size(); j++) {
    				((HazardDAO)getDao(Table.HAZARD)).save(pathHazards.get(j));
    			}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save hazard");
			}	
    	}

   	
    	
    	Enumeration<Bus> busEnum =    martaModel.getAllFuelConsumptions().keys();
    	while(busEnum.hasMoreElements()) {
    		Bus bus = busEnum.nextElement();
    		ArrayList<FuelConsumption> fuelConsumption= martaModel.getFuelConsumptionList(bus);
    		try {
    			for (int j = 0; j < fuelConsumption.size(); j++) {
    				((FuelConsumptionDAO)getDao(Table.FUELCONSUMPTION)).save(fuelConsumption.get(j));
    			}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Unable to save fuel consumption");
			}	
    	}


    	//route definitions
    	// bus routes
//    	System.out.printf("saving bus route definitions\n");
//    	Enumeration<Integer> brouteIDEnum = martaModel.getBusRoutes().keys();
//    	while(brouteIDEnum.hasMoreElements()){
//    		BusRoute route = martaModel.getBusRoutes().get(brouteIDEnum.nextElement());
//    	    System.out.printf("route %s has %s stops\n",route.getName(), route.getLength());
//    		for(int stopLocation=0;stopLocation<route.getLength();stopLocation++){
//    			System.out.println("route "+route.getName()+" has "+route.getLength()+" locations");
//    			Facility facility = route.getBusStop(martaModel, stopLocation);
//    			RouteDefinition rdef = new RouteDefinition(route, stopLocation, facility);
//    			((RouteDefinitionDAO)getDao(Table.ROUTEDEFINITION)).save(rdef);
//    		}
//    	}
//
//    	System.out.printf("saving bus route definitions\n");
//    	Enumeration<Integer> rrouteIDEnum = martaModel.getRailRoutes().keys();
//    	while(rrouteIDEnum.hasMoreElements()){
//    		RailRoute route = martaModel.getRailRoutes().get(rrouteIDEnum.nextElement());
//    	    System.out.printf("route %s has %s stops\n",route.getName(), route.getLength());
//    		for(int stopLocation=0;stopLocation<route.getLength();stopLocation++){
//    			System.out.println("route "+route.getName()+" has "+route.getLength()+" locations");
//    			Facility facility = route.getRailStation(martaModel, stopLocation);
//    			RouteDefinition rdef = new RouteDefinition(route, stopLocation, facility);
//    			((RouteDefinitionDAO)getDao(Table.ROUTEDEFINITION)).save(rdef);
//    		}
//    	}
    	
    	//persisting events
    	for(SimEvent event: simEngine.getEvents()) {
    		System.out.printf("eventtype: %s\n",event.getType());
    		switch(event.getType()) {
    		case "set_path_delay":
    			getDao(Table.SETPATHDELAYEVENT).save(event);
    			break;
    		case "clear_path_delay":
    			getDao(Table.CLEARPATHDELAYEVENT).save(event);
    			break;
    		case "set_speed_limit":
    			getDao(Table.SETSPEEDLIMITEVENT).save(event);
    			break;
    		case "clear_speed_limit":
    			getDao(Table.CLEARSPEEDLIMITEVENT).save(event);
    			break;
    		case "exchangePoint_out_of_service":
    			getDao(Table.FACILITYOUTOFSERVICEEVENT).save(event);
    			break;
    		case "exchangePoint_resumed_service":
    			getDao(Table.FACILITYRESUMESERVICEEVENT).save(event);
    			break;
    		case "move_bus":
    			getDao(Table.MOVEBUSEVENT).save(event);
    			break;
    		case "move_train":
    			getDao(Table.MOVETRAINEVENT).save(event);
    			break;
    		case "vehicle_out_of_service":
    			getDao(Table.VEHICLEOUTOFSERVICEEVENT).save(event);
    			break;
    		case "vehicle_resumed_service":
    			getDao(Table.VEHICLERESUMESERVICEEVENT).save(event);
    			break;
    		case "set_path_block":
    			getDao(Table.BLOCKPATHEVENT).save(event);
    			break;
    		}
    	}
    	
    	System.out.printf("system state persisted\n");
        }
    	return true;
	}

	public void checkPriorSim() throws ClassNotFoundException, SQLException, Exception {
		if(this.persistenceOn) {
			System.out.printf("checking for prior sims\n");
			hasPriorSim = hasSavedSimulation();
			System.out.printf("hasPriorSim = %s\n",Boolean.toString(hasPriorSim));
		}
		
	}

	@SuppressWarnings("unchecked")
	public void restoreSim() throws ClassNotFoundException, SQLException, Exception {
		//restoring core entities from database
		ArrayList<Depot> depots = getDao(Table.DEPOT).find();
		if(depots !=null && depots.size()>0) martaModel.setDepot(depots.get(0));
		else System.out.printf("no depots\n");
		
		ArrayList<BusStop> busStops = getDao(Table.BUSSTOP).find();
		for(BusStop stop : busStops) {martaModel.getStops().put(stop.get_uniqueID(), stop);};
		ArrayList<BusRoute> busRoutes = getDao(Table.BUSROUTE).find();
		for(BusRoute route : busRoutes) {martaModel.getBusRoutes().put(route.getID(), route);};
		ArrayList<Bus> buses = getDao(Table.BUS).find();
		for(Bus vehicle : buses) {martaModel.getBuses().put(vehicle.getID(), vehicle);};

		ArrayList<RailStation> trainStops = getDao(Table.RAILSTATION).find();
		for(RailStation stop : trainStops) {martaModel.getRailStations().put(stop.get_uniqueID(), stop);};
		ArrayList<RailRoute> railRoutes = getDao(Table.RAILROUTE).find();
		for(RailRoute route : railRoutes) {martaModel.getRailRoutes().put(route.getID(), route);};
		ArrayList<RailCar> trains = getDao(Table.RAILCAR).find();
		for(RailCar vehicle : trains) {martaModel.getTrains().put(vehicle.getID(), vehicle);};
		

		ArrayList<Hazard> hazards = getDao(Table.HAZARD).find();
		for(Hazard hazard : hazards) {
			ArrayList<Hazard> pathHazards = new ArrayList<Hazard>();
			pathHazards.add(hazard);
			martaModel.getAllHazards().put(hazard.getPathKey(), pathHazards);};
			

		
		//Route definitions
		ArrayList<RouteDefinition> definitions = getDao(Table.ROUTEDEFINITION).find();
		
		for(RouteDefinition rdef : definitions) {
			if(rdef.getRoute().getType().equals("busRoute")) {
				System.out.println("extending bus route");
				BusRoute br = (BusRoute)rdef.getRoute();
				br.addNewStop(rdef.getFacility().get_uniqueID());
			}
			if(rdef.getRoute().getType().equals("railRoute")) {
				System.out.println("extending rail route");
				RailRoute rr = (RailRoute)rdef.getRoute();
				rr.addNewStation(rdef.getFacility().get_uniqueID());
			}
		}

		ArrayList<Path> paths = getDao(Table.PATH).find();
		for(Path path : paths) {martaModel.getPaths().put(path.getPathKey(),path);};

		ArrayList<FuelConsumption> reportsDB = getDao(Table.FUELCONSUMPTION).find();
		for(FuelConsumption report : reportsDB) {
			ArrayList<FuelConsumption> busReports = martaModel.getFuelConsumptionList(report.getBus());
			busReports.add(report);
		};
		
		
		//restoring events from database
		System.out.printf("restoring events from DB.  event queue had %d events.\n", simEngine.getSize());
		ArrayList<SimEvent> events;
		events = getDao(Table.SETPATHDELAYEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.CLEARPATHDELAYEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.SETSPEEDLIMITEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.CLEARSPEEDLIMITEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.FACILITYOUTOFSERVICEEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.FACILITYRESUMESERVICEEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.MOVEBUSEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.MOVETRAINEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.VEHICLEOUTOFSERVICEEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.VEHICLERESUMESERVICEEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		events = getDao(Table.BLOCKPATHEVENT).find();
		for(int i=0;i<events.size();i++) {simEngine.add(events.get(i));}
		System.out.printf("event queue now has %d events.\n", simEngine.getSize());
		
	}
}
