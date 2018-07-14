package group_a7_8;

import java.util.Hashtable;

import group_a7_8.DAOManager.Table;

public class DBTest {

	public static void main(String[] args) throws Exception {
		//at startup
		DAOManager dao = DAOManager.getInstance();
		System.out.println("got dao");
		
		dao.dropAll();
		dao.createDB();
		
		//operational code
		BusStopDAO busStopDao = (BusStopDAO)dao.getDAO(Table.BUSSTOP);
		BusRouteDAO busRouteDao = (BusRouteDAO)dao.getDAO(Table.BUSROUTE);
		BusDAO busDao = (BusDAO)dao.getDAO(Table.BUS);
		System.out.println("system has "+busStopDao.count()+" bus stops");
		System.out.println("system has "+busRouteDao.count()+" bus routes");
		System.out.println("system has "+busDao.count()+" buses");

		Hashtable<Integer,BusStop> busStops = new Hashtable<Integer,BusStop>();
		Hashtable<Integer,BusRoute> busRoutes = new Hashtable<Integer,BusRoute>();
		Hashtable<Integer,Bus> buses = new Hashtable<Integer,Bus>();
		
		for(int i=0;i<2;i++) {
			busRoutes.put(i, new BusRoute(i,i,String.format("r%d", i)));
		}
		for(int i=0;i<8;i++) {
			busStops.put(i, new BusStop(i,String.format("r%d.%d", i%2,i),i,i));
		}
		for(int i=0;i<5;i++) {
			buses.put(i, new Bus(i,i%2));
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
		System.out.println("system has "+busStopDao.count()+" bus stops");
		System.out.println("system has "+busRouteDao.count()+" bus routes");
		System.out.println("system has "+busDao.count()+" buses");

		//clear the caches
		busStops.clear();
		busRoutes.clear();
		buses.clear();
		System.out.println("bus chache "+buses.size()+" entries");
		System.out.println("bus route chache "+busRoutes.size()+" entries");
		System.out.println("bus stop chache "+busStops.size()+" entries");
		
		//retrieve the entries from the database
		for(BusStop stop : busStopDao.find()) {
			busStops.put(stop.getId(), stop);
		}
		for(BusRoute route : busRouteDao.find()) {
			busRoutes.put(route.getId(), route);
		}
		for(Bus bus : busDao.find()) {
			buses.put(bus.getID(), bus);
		}
		System.out.println("bus chache "+buses.size()+" entries");
		System.out.println("bus route chache "+busRoutes.size()+" entries");
		System.out.println("bus stop chache "+busStops.size()+" entries");
		
		
		dao.clearDB();

		System.out.println("system has "+busStopDao.count()+" bus stops");
		System.out.println("system has "+busRouteDao.count()+" bus routes");
		System.out.println("system has "+busDao.count()+" buses");
		
		//at shut down
		dao.close();
		System.out.printf("terminating ...\n");
	}

}
