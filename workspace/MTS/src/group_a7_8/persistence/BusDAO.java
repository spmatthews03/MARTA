package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Bus;


public class BusDAO extends GenericDAO<Bus>{

	protected BusDAO(Connection con) {
		super(con, "VEHICLE", "type", "Bus");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,'%s',%d,%d,%d,%d,%f,%f,%d)";
	
	
	@Override
	public void save(Bus bus) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"vehicleLogicalID","type","routeLogicalID","location",
				"passengers","capacity","fuellevel","fuelcapacity","speed",
				bus.getID(),bus.getType(),bus.getRouteID(),(int)bus.getLocation(),
				(int)bus.getPassengers(),(int)bus.getCapacity(),bus.getFuelLevel(),bus.getFuelCapacity(),(int)bus.getSpeed()));
		
		stmt.execute(String.format(insert_format,tableName,
				"vehicleLogicalID","type","routeLogicalID","location",
				"passengers","capacity","fuellevel","fuelcapacity","speed",
				bus.getID(),bus.getType(),bus.getRouteID(),bus.getLocation(),
				bus.getPassengers(),bus.getCapacity(),bus.getFuelLevel(),bus.getFuelCapacity(),bus.getSpeed()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<Bus> find() throws SQLException {
		ArrayList<Bus> buses = new ArrayList<Bus>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"vehicleLogicalID","routeLogicalID","location","passengers",
				"capacity","fuellevel","fuelcapacity","speed",
				tableName,"type","bus"));
		while(rs.next()) {
			Bus vehicle = new Bus(null,
					   rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),
					   rs.getDouble(6),rs.getDouble(7),rs.getInt(8)
					   );
		   buses.add(vehicle);
		   System.out.printf("retrieved %s\n", vehicle);
		}
		return buses;
	}

}
