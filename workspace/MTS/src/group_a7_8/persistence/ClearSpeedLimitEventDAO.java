package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.PathKey;
import group_a7_8.event.ClearSpeedLimitEvent;


public class ClearSpeedLimitEventDAO extends GenericDAO<ClearSpeedLimitEvent>{

	protected ClearSpeedLimitEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system, eventQueue, con, "EVENT", "type", "clear_speed_limit");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,'%s',%d,%d,'%s',%d)";

	@Override
	public void save(ClearSpeedLimitEvent clearSpeedLimitEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"timeRank",
				"type",
				"eventID",
				clearSpeedLimitEvent.getPathKey().getOrigin().getType(),
				clearSpeedLimitEvent.getPathKey().getOrigin().get_uniqueID(),
				clearSpeedLimitEvent.getPathKey().getDestination().getType(),
				clearSpeedLimitEvent.getPathKey().getDestination().get_uniqueID(),
				clearSpeedLimitEvent.getRank(),
				filterValue,
				clearSpeedLimitEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"timeRank",
				"type",
				"eventID",
				clearSpeedLimitEvent.getPathKey().getOrigin().getType(),
				clearSpeedLimitEvent.getPathKey().getOrigin().get_uniqueID(),
				clearSpeedLimitEvent.getPathKey().getDestination().getType(),
				clearSpeedLimitEvent.getPathKey().getDestination().get_uniqueID(),
				clearSpeedLimitEvent.getRank(),
				filterValue,
				clearSpeedLimitEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<ClearSpeedLimitEvent> find() throws SQLException {
		ArrayList<ClearSpeedLimitEvent> clearSpeedLimitEvents = new ArrayList<ClearSpeedLimitEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"eventID",
				"timeRank",
				"type",
				tableName,"type",filterValue));
		while(rs.next()) {
			Facility origin = getFacility(rs.getString(1),rs.getInt(2));
			Facility destination = getFacility(rs.getString(3),rs.getInt(4));
			PathKey pk = new PathKey(origin, destination);

			ClearSpeedLimitEvent clearSpeedLimitEvent= new ClearSpeedLimitEvent(system,rs.getInt(5),rs.getInt(6),pk);
			clearSpeedLimitEvents.add(clearSpeedLimitEvent);
		   System.out.printf("retrieved %s\n", clearSpeedLimitEvent);
		}
		return clearSpeedLimitEvents;
	}

}
