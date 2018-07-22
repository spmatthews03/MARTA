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
import group_a7_8.FileProps;
import group_a7_8.persistence.DAOManager.Table;

public class DBTest {
	private static final String CONFIG_PATH_TOKEN="-config:";
	private static final String SCENARIO_TOKEN="-scenario:";
	private static final String DBTEST_TOKEN="dbtest";
	private static final String CLEARDB_TOKEN="cleardb";
	private static final String CHECKDB_TOKEN="checkdb";
	private static final String CREATEDB_TOKEN="createdb";
	@SuppressWarnings("rawtypes")
	private Hashtable<Table,GenericDAO> daos=new Hashtable<Table,GenericDAO>();

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
	  
	  DBTest instance = new DBTest();
	  instance.run(scenario);
		

		

		
		//at shut down
		DAOManager.getInstance().close();
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
		default:
			this.dbTest();
		}
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
		System.out.println("system has " + busStopDao.count() + " bus stops");
		System.out.println("system has " + busRouteDao.count() + " bus routes");
		System.out.println("system has " + busDao.count() + " buses");
		System.out.println("system has " + railStationDao.count() + " rail stations");
		System.out.println("system has " + railRouteDao.count() + " rail routes");
		System.out.println("system has " + railCarDao.count() + " rail cars");

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
		
		
		checkDB();		
	}
	public void clearDB() throws Exception{
		DAOManager.getInstance().clearDB();
	}
	public void createDB() throws Exception{
		DAOManager.getInstance().dropAll();
		DAOManager.getInstance().createDB();
	}
	@SuppressWarnings("rawtypes")
	public GenericDAO getDAO(Table t) throws ClassNotFoundException, SQLException, Exception {
		if(!daos.containsKey(t)) {
			daos.put(t, DAOManager.getInstance().getDAO(t));
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
