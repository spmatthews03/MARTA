package group_a7_8.persistence;

import java.sql.SQLException;
import java.util.Hashtable;

import edu.gatech.Bus;
import edu.gatech.BusRoute;
import edu.gatech.BusStop;
import edu.gatech.Depot;
import edu.gatech.RailCar;
import edu.gatech.RailRoute;
import edu.gatech.RailStation;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.FileProps;
import group_a7_8.Path;
import group_a7_8.PathKey;
import group_a7_8.event.BlockPathEvent;
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
import group_a7_8.persistence.DAOManager.Table;

public class DBTest {
	private static final String CONFIG_PATH_TOKEN="-config:";
	private static final String SCENARIO_TOKEN="-scenario:";
	private static final String DBTEST_TOKEN="dbtest";
	private static final String CLEARDB_TOKEN="cleardb";
	private static final String CHECKDB_TOKEN="checkdb";
	private static final String CREATEDB_TOKEN="createdb";
	private static final String TESTEVENTS_TOKEN="testevents";
	@SuppressWarnings("rawtypes")
	private Hashtable<Table,GenericDAO> daos=new Hashtable<Table,GenericDAO>();
	private TransitSystem system;
	private SimQueue eventQueue;

	public DBTest(TransitSystem system, SimQueue eventQueue) {
		this.system = system;
		this.eventQueue = eventQueue;
	}

	public static void main(String[] args) throws Exception {

	  String scenario=null;
	  for(String arg:args) {
		  if(arg.startsWith(CONFIG_PATH_TOKEN)) {
			  FileProps.SetConfigPath(arg.substring(CONFIG_PATH_TOKEN.length()).trim());
		  }
		  if(arg.startsWith(SCENARIO_TOKEN)) {
			  scenario = arg.substring(SCENARIO_TOKEN.length()).trim();
		  }
	  }
	  
	  System.out.printf("DBTest started with scenario %s\n",scenario);
		// set up transit system
		TransitSystem system = new TransitSystem();
		SimQueue eventQueue = new SimQueue();
	  
	  DBTest instance = new DBTest(system,eventQueue);
	  instance.run(scenario);
		

		

		
		//at shut down
		DAOManager.getInstance(system,eventQueue).close();
		System.out.printf("terminating ...\n");
	}
	
	private void run(String scenario) throws Exception {
		if(scenario==null) scenario= DBTEST_TOKEN;
		switch(scenario) {
		case CREATEDB_TOKEN:
			createDB();
			break;
		case CLEARDB_TOKEN:
			clearDB();
			break;
		case CHECKDB_TOKEN:
			checkDB();
			break;
		case DBTEST_TOKEN:
			dbTest();
			break;
		case TESTEVENTS_TOKEN:
			testEvents();
			break;
		default:
			this.dbTest();
		}
	}

	private void testEvents() throws ClassNotFoundException, SQLException, Exception {
		//at startup
		createDB();
		
		//eventDAOs
		//BlockPathEventDAO blockPathEventDAO = (BlockPathEventDAO)getDAO(Table.BLOCKPATHEVENT);
		ClearPathDelayEventDAO clearPathDelayEventDAO = (ClearPathDelayEventDAO)getDAO(Table.CLEARPATHDELAYEVENT);
		ClearSpeedLimitEventDAO clearSpeedLimitEventDAO = (ClearSpeedLimitEventDAO)getDAO(Table.CLEARSPEEDLIMITEVENT);
		FacilityOutOfServiceEventDAO facilityOutOfServiceEventDAO = (FacilityOutOfServiceEventDAO)getDAO(Table.FACILITYOUTOFSERVICEEVENT);
		FacilityResumeServiceEventDAO facilityResumeServiceEventDAO = (FacilityResumeServiceEventDAO)getDAO(Table.FACILITYRESUMESERVICEEVENT);
		MoveBusEventDAO moveBusEventDAO = (MoveBusEventDAO)getDAO(Table.MOVEBUSEVENT);
		MoveTrainEventDAO moveTrainEventDAO = (MoveTrainEventDAO)getDAO(Table.MOVETRAINEVENT);
		SetPathDelayEventDAO setPathDelayEventDAO = (SetPathDelayEventDAO)getDAO(Table.SETPATHDELAYEVENT);
		SetSpeedLimitEventDAO setSpeedLimitEventDAO = (SetSpeedLimitEventDAO)getDAO(Table.SETSPEEDLIMITEVENT);
		VehicleOutOfServiceEventDAO vehicleOutOfServiceEventDAO = (VehicleOutOfServiceEventDAO)getDAO(Table.VEHICLEOUTOFSERVICEEVENT);
		VehicleResumeServiceEventDAO vehicleResumeServiceEventDAO = (VehicleResumeServiceEventDAO)getDAO(Table.VEHICLERESUMESERVICEEVENT);
		BlockPathEventDAO blockPathEventDAO = (BlockPathEventDAO)getDAO(Table.BLOCKPATHEVENT);
		checkDB();


		// bus route
		system.makeStop(1, "bs 1", 1, 1.1, 1.2);
		BusStop bs1 = system.getBusStop(1);
		system.makeStop(2, "bs 2", 2, 2.1, 2.2);
		BusStop bs2 = system.getBusStop(2);
		system.makeBusRoute(1, 1, "br 1");  
		BusRoute br1 = system.getBusRoute(1);
		system.appendStopToRoute(1, 1);
		system.appendStopToRoute(1, 2);
		PathKey pk_bs1_bs2 = new PathKey(bs1, bs2);
		Path p_bs1_bs2 = system.getPath(pk_bs1_bs2);
		system.makeBus(1, 1, 0, 2, 10, 50, 200,50);
		Bus b1 = system.getBus(1);
		// train route
		  //intentionally reusing logical ids
		system.makeRailStation(1, "rs 1", 3, 3.1, 3.2);
		RailStation ts1 = system.getRailStation(1);
		system.makeRailStation(2, "rs 2", 4, 4.1, 4.2);
		RailStation ts2 = system.getRailStation(2);
		system.makeRailRoute(1, 1, "rr 1"); 
		RailRoute tr1 = system.getRailRoute(1);
		system.appendStationToRoute(1, 1);
		system.appendStationToRoute(1, 2);
		PathKey pk_ts1_ts2 = new PathKey(ts1, ts2);
		Path p_ts1_ts2 = system.getPath(pk_ts1_ts2);
		system.makeTrain(1, 1, 0, 2, 10,50);
		RailCar t1 = system.getTrain(1);

		
		
		Hashtable<Integer,SimEvent> events = new Hashtable<Integer,SimEvent>();
		events.put(1, new SetPathDelayEvent(system, 1, 1, pk_bs1_bs2,2));
		events.put(2, new ClearPathDelayEvent(system, 2, 2, pk_bs1_bs2,2));
		events.put(3, new SetSpeedLimitEvent(system, 3, 3, pk_ts1_ts2,20));
		events.put(4, new ClearSpeedLimitEvent(system, 4, 4, pk_ts1_ts2));
		events.put(5, new FacilityOutOfServiceEvent(system, 5, 5, bs1));
		events.put(6, new FacilityResumeServiceEvent(system, 6, 6, bs2));
		events.put(7, new MoveBusEvent(system, 7, 7, b1));
		events.put(8, new MoveTrainEvent(system, 8, 8, t1));
		events.put(9, new VehicleOutOfServiceEvent(system, 9, 9, b1,10,10));
		events.put(10, new VehicleResumeServiceEvent(system, 10, 10, b1));
		events.put(11, new BlockPathEvent(system, 1, 1, t1,p_ts1_ts2));
		System.out.println("system has " + events.size() + " events in cache");
		
		//save events
		setPathDelayEventDAO.save((SetPathDelayEvent)events.get(1));
		clearPathDelayEventDAO.save((ClearPathDelayEvent)events.get(2));
		setSpeedLimitEventDAO.save((SetSpeedLimitEvent)events.get(3));
		clearSpeedLimitEventDAO.save((ClearSpeedLimitEvent)events.get(4));
		facilityOutOfServiceEventDAO.save((FacilityOutOfServiceEvent)events.get(5));
		facilityResumeServiceEventDAO.save((FacilityResumeServiceEvent)events.get(6));
		moveBusEventDAO.save((MoveBusEvent)events.get(7));
		moveTrainEventDAO.save((MoveTrainEvent)events.get(8));
		vehicleOutOfServiceEventDAO.save((VehicleOutOfServiceEvent)events.get(9));
		vehicleResumeServiceEventDAO.save((VehicleResumeServiceEvent)events.get(10));
		blockPathEventDAO.save((BlockPathEvent)events.get(11));
		checkDB();
		
		//clear cache
		events.clear();
		System.out.println("system has " + events.size() + " events in cache");
		
		//restore events from db
		int i=0;
		//for(BlockPathEvent event : blockPathEventDAO.find()) { events.put(i++, event); }
		for(ClearPathDelayEvent event : clearPathDelayEventDAO.find()) { events.put(i++, event); }
		for(ClearSpeedLimitEvent event : clearSpeedLimitEventDAO.find()) { events.put(i++, event); }
		for(FacilityOutOfServiceEvent event : facilityOutOfServiceEventDAO.find()) { events.put(i++, event); }
		for(FacilityResumeServiceEvent event : facilityResumeServiceEventDAO.find()) { events.put(i++, event); }
		for(MoveBusEvent event : moveBusEventDAO.find()) { events.put(i++, event); }
		for(MoveTrainEvent event : moveTrainEventDAO.find()) { events.put(i++, event); }
		for(SetPathDelayEvent event : setPathDelayEventDAO.find()) { events.put(i++, event); }
		for(SetSpeedLimitEvent event : setSpeedLimitEventDAO.find()) { events.put(i++, event); }
		for(VehicleOutOfServiceEvent event : vehicleOutOfServiceEventDAO.find()) { events.put(i++, event); }
		for(VehicleResumeServiceEvent event : vehicleResumeServiceEventDAO.find()) { events.put(i++, event); }
		for(VehicleResumeServiceEvent event : vehicleResumeServiceEventDAO.find()) { events.put(i++, event); }
		for(BlockPathEvent event : blockPathEventDAO.find()) { events.put(i++, event); }
		System.out.println("system has " + events.size() + " events in cache");
		
		//remove test data from db
		clearDB();
		checkDB();		

	}

	public void dbTest() throws ClassNotFoundException, SQLException, Exception {
		//at startup
		createDB();
		
		
		//operational code
		BusStopDAO busStopDao = (BusStopDAO)getDAO(Table.BUSSTOP);
		BusRouteDAO busRouteDao = (BusRouteDAO)getDAO(Table.BUSROUTE);
		BusDAO busDao = (BusDAO)getDAO(Table.BUS);
		DepotDAO depotDao = (DepotDAO)getDAO(Table.DEPOT);
		RailCarDAO railCarDao = (RailCarDAO)getDAO(Table.RAILCAR);
		RailRouteDAO railRouteDao = (RailRouteDAO)getDAO(Table.RAILROUTE);
		RailStationDAO railStationDao = (RailStationDAO)getDAO(Table.RAILSTATION);
		
		checkDB();
		
		Hashtable<Integer,BusStop> busStops = new Hashtable<Integer,BusStop>();
		Hashtable<Integer,BusRoute> busRoutes = new Hashtable<Integer,BusRoute>();
		Hashtable<Integer,Bus> buses = new Hashtable<Integer,Bus>();
		Hashtable<Integer,Depot> depots = new Hashtable<Integer,Depot>();
		Hashtable<Integer,RailCar> railCars = new Hashtable<Integer,RailCar>();
		Hashtable<Integer,RailRoute> railRoutes = new Hashtable<Integer,RailRoute>();
		Hashtable<Integer,RailStation> railStations = new Hashtable<Integer,RailStation>();

		for(int i=0;i<2;i++) {
			busRoutes.put(i, new BusRoute(i,i,String.format("r%d", i)));
		}
		for(int i=0;i<8;i++) {
			busStops.put(i, new BusStop(i,String.format("r%d.%d", i%2,i),i,(double)i,(double)i));
		}
		for(int i=0;i<5;i++) {
			buses.put(i, new Bus( null,i,i%2));
		}
		for(int i=0;i<2;i++) {
			railRoutes.put(i, new RailRoute(i,i,String.format("r%d", i)));
		}
		for(int i=0;i<8;i++) {
			railStations.put(i, new RailStation(i,String.format("r%d.%d", i%2,i),i,(double)i,(double)i));
		}
		for(int i=0;i<5;i++) {
			railCars.put(i, new RailCar( i ));
		}
		
		//update state
		//save buses
		for(Bus bus : buses.values()) {
			busDao.save(bus);
		}
		//save busstops
		for(BusStop stop: busStops.values()) {
			busStopDao.save(stop);
		}		
		//save busroutes
		for(BusRoute route: busRoutes.values()) {
			busRouteDao.save(route);
		}		
		//save rail cars
		for(RailCar railCar : railCars.values()) {
			railCarDao.save(railCar);
		}
		//save rail stations
		for(RailStation railStation: railStations.values()) {
			railStationDao.save(railStation);
		}		
		//save rail routes
		for(RailRoute route: railRoutes.values()) {
			railRouteDao.save(route);
		}		

		checkDB();

		//clear the caches
		busStops.clear();
		busRoutes.clear();
		buses.clear();
		railStations.clear();
		railRoutes.clear();
		railCars.clear();
		System.out.println("bus cache " + buses.size() + " entries");
		System.out.println("bus route cache " + busRoutes.size( ) + " entries");
		System.out.println("bus stop cache " + busStops.size() + " entries");
		System.out.println("rail car cache " + railCars.size() + " entries");
		System.out.println("rail route cache " + railRoutes.size() + " entries");
		System.out.println("rail station cache " + railStations.size() + " entries");
		
		//retrieve the entries from the database
		for(BusStop stop : busStopDao.find()) {
			busStops.put(stop.get_uniqueID(), stop);
		}
		for(BusRoute route : busRouteDao.find()) {
			busRoutes.put(route.getID(), route);
		}
		for(Bus bus : busDao.find()) {
			buses.put(bus.getID(), bus);
		}
		for(RailStation station : railStationDao.find()) {
			railStations.put(station.get_uniqueID(), station);
		}
		for(RailRoute route : railRouteDao.find()) {
			railRoutes.put(route.getID(), route);
		}
		for(RailCar railCar : railCarDao.find()) {
			railCars.put(railCar.getID(), railCar);
		}
		
		System.out.println("bus cache " + buses.size() + " entries");
		System.out.println("bus route cache " + busRoutes.size( ) + " entries");
		System.out.println("bus stop cache " + busStops.size() + " entries");
		System.out.println("rail car cache " + railCars.size() + " entries");
		System.out.println("rail route cache " + railRoutes.size() + " entries");
		System.out.println("rail station cache " + railStations.size() + " entries");
		
		clearDB();
		checkDB();		
	}
	public void clearDB() throws Exception{
		DAOManager.getInstance(system,eventQueue).clearDB();
	}
	public void createDB() throws Exception{
		DAOManager.getInstance(system,eventQueue).dropAll();
		DAOManager.getInstance(system,eventQueue).createDB();
	}
	@SuppressWarnings("rawtypes")
	public GenericDAO getDAO(Table t) throws ClassNotFoundException, SQLException, Exception {
		if(!daos.containsKey(t)) {
			daos.put(t, DAOManager.getInstance(system,eventQueue).getDAO(t));
		}
		return daos.get(t);
	}
	@SuppressWarnings("rawtypes")
	public void checkDB() throws ClassNotFoundException, SQLException, Exception{
		for(Table t : Table.values()) {
			GenericDAO entity = getDAO(t);
			System.out.printf("system has %d %s\n",entity.count(),t.toString());
		}
	}

}
