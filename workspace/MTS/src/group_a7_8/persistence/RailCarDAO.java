package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.RailCar;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;


public class RailCarDAO extends GenericDAO<RailCar>{

	protected RailCarDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "VEHICLE", "type", "Train");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,'%s',%d,%d,%d,%d,%d)";
	
	
	@Override
	public void save(RailCar railCar) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"vehicleLogicalID",
				"type",
				"routeLogicalID",
				"location",
				"passengers",
				"capacity",
				"speed",
				railCar.getID(),
				filterValue,
				railCar.getRouteID(),
				(int)railCar.getLocation(),
				(int)railCar.getPassengers(),
				(int)railCar.getCapacity(),
				(int)railCar.getSpeed()));
		
		stmt.execute(String.format(insert_format,tableName,
				"vehicleLogicalID",
				"type",
				"routeLogicalID",
				"location",
				"passengers",
				"capacity",
				"speed",
				railCar.getID(),
				filterValue,
				railCar.getRouteID(),
				railCar.getLocation(),
				railCar.getPassengers(),
				railCar.getCapacity(),
				railCar.getSpeed()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<RailCar> find() throws SQLException {
		ArrayList<RailCar> railCars = new ArrayList<RailCar>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"vehicleLogicalID",
				"routeLogicalID",
				"location",
				"passengers",
				"capacity",
				"speed",
				tableName, "type", filterValue));
		while(rs.next()) {
			RailCar vehicle = new RailCar(
					   rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
					   rs.getInt(6)
					   );
			vehicle.system = this.system;
	    	int inputRoute = rs.getInt(2);
	    	vehicle.set_rail_route(this.system.getRailRoute(inputRoute));

			railCars.add(vehicle);
		    System.out.printf("retrieved %s\n", vehicle);
		}
		return railCars;
	}

}
