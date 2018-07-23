package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.Hazard;
import group_a7_8.PathKey;


public class HazardDAO extends GenericDAO<Hazard>{

	protected HazardDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "HAZARD", "true", "true");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,%d,'%s','%s',%f,'%s')";
	
	
	@Override
	public void save(Hazard hazard) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"delayFactor",
				"type",
				hazard.getPathKey().getOrigin().get_uniqueID(),
				hazard.getPathKey().getDestination().get_uniqueID(),
				hazard.getPathKey().getOrigin().getType(),
				hazard.getPathKey().getDestination().getType(),
				hazard.getDelayFactor(),
				filterValue
				));
		
		stmt.execute(String.format(insert_format,tableName,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"delayFactor",
				"type",
				hazard.getPathKey().getOrigin().get_uniqueID(),
				hazard.getPathKey().getDestination().get_uniqueID(),
				hazard.getPathKey().getOrigin().getType(),
				hazard.getPathKey().getDestination().getType(),
				hazard.getDelayFactor(),
				filterValue
				));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<Hazard> find() throws SQLException {
		ArrayList<Hazard> hazards = new ArrayList<Hazard>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"delayFactor",
				tableName,"type",filterValue));
		while(rs.next()) {
			Facility origin = getFacility(rs.getString(3),rs.getInt(1));
			Facility destination = getFacility(rs.getString(4),rs.getInt(2));
			PathKey pk = new PathKey(origin, destination);

			Hazard hazard = new Hazard(pk, rs.getDouble(5));
			hazards.add(hazard);
		   System.out.printf("retrieved %s\n", hazard);
		}
		return hazards;
	}

}
