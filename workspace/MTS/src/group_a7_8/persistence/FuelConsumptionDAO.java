package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Bus;
import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.FuelConsumption;
import group_a7_8.PathKey;


public class FuelConsumptionDAO extends GenericDAO<FuelConsumption>{

	protected FuelConsumptionDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "FUELCONSUMPTION", "type", "fuelConsumption");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,%f,%d,%d,'%s',%d,%d,'%s','%s','%s')";
	
	
	@Override
	public void save(FuelConsumption fuelConsumption) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"timeRank",
				"amount",
				"passengers",
				"vehicleID",
				"vehicleType",
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"type",
				fuelConsumption.getTimeRank(),
				fuelConsumption.getFuelConsumed(),
				fuelConsumption.getPassengers(),
				fuelConsumption.getBus().getID(),
				fuelConsumption.getBus().getType(),
				fuelConsumption.getPathKey().getOrigin().get_uniqueID(),
				fuelConsumption.getPathKey().getDestination().get_uniqueID(),
				fuelConsumption.getPathKey().getOrigin().getType(),
				fuelConsumption.getPathKey().getDestination().getType(),
				filterValue));
		
		stmt.execute(String.format(insert_format,tableName,
				"timeRank",
				"amount",
				"passengers",
				"vehicleID",
				"vehicleType",
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"type",
				fuelConsumption.getTimeRank(),
				fuelConsumption.getFuelConsumed(),
				fuelConsumption.getPassengers(),
				fuelConsumption.getBus().getID(),
				fuelConsumption.getBus().getType(),
				fuelConsumption.getPathKey().getOrigin().get_uniqueID(),
				fuelConsumption.getPathKey().getDestination().get_uniqueID(),
				fuelConsumption.getPathKey().getOrigin().getType(),
				fuelConsumption.getPathKey().getDestination().getType(),
				filterValue));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FuelConsumption> find() throws SQLException {
		ArrayList<FuelConsumption> fuelConsumptions = new ArrayList<FuelConsumption>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"timeRank",
				"amount",
				"passengers",
				"vehicleID",
				"vehicleType",
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				tableName,"type",filterValue));
		while(rs.next()) {
			Bus bus = system.getBus(rs.getInt(4));
			Facility origin = getFacility(rs.getString(8).trim(),rs.getInt(6));
			Facility destination = getFacility(rs.getString(9).trim(),rs.getInt(7));
			PathKey pk = new PathKey(origin, destination);

			FuelConsumption fuelConsumption = new FuelConsumption(bus, pk, 
					   rs.getInt(1), rs.getDouble(2), rs.getInt(3));
			fuelConsumptions.add(fuelConsumption);
		   System.out.printf("retrieved %s\n", fuelConsumption.toJSON());
		}
		return fuelConsumptions;
	}

}
