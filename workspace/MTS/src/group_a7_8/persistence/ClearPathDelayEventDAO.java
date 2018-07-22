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
import group_a7_8.event.ClearPathDelayEvent;



public class ClearPathDelayEventDAO extends GenericDAO<ClearPathDelayEvent>{

	protected ClearPathDelayEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "clear_path_delay");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,'%s',%d,%f,%d,'%s',%d)";

	@Override
	public void save(ClearPathDelayEvent clearPathDelayEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"delayFactor",
				"timeRank",
				"type",
				"eventID",
				clearPathDelayEvent.getPathKey().getOrigin().getType(),
				clearPathDelayEvent.getPathKey().getOrigin().get_uniqueID(),
				clearPathDelayEvent.getPathKey().getDestination().getType(),
				clearPathDelayEvent.getPathKey().getDestination().get_uniqueID(),
				clearPathDelayEvent.getDelayFactor(),
				clearPathDelayEvent.getRank(),
				filterValue,
				clearPathDelayEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"delayFactor",
				"timeRank",
				"type",
				"eventID",
				clearPathDelayEvent.getPathKey().getOrigin().getType(),
				clearPathDelayEvent.getPathKey().getOrigin().get_uniqueID(),
				clearPathDelayEvent.getPathKey().getDestination().getType(),
				clearPathDelayEvent.getPathKey().getDestination().get_uniqueID(),
				clearPathDelayEvent.getDelayFactor(),
				clearPathDelayEvent.getRank(),
				filterValue,
				clearPathDelayEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<ClearPathDelayEvent> find() throws SQLException {
		ArrayList<ClearPathDelayEvent> clearPathDelayEvents = new ArrayList<ClearPathDelayEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"eventID",
				"timeRank",
				"delayFactor",
				"type",
				tableName,"type",filterValue));
		while(rs.next()) {
			Facility origin = getFacility(rs.getString(1),rs.getInt(2));
			Facility destination = getFacility(rs.getString(3),rs.getInt(4));
			PathKey pk = new PathKey(origin, destination);
			ClearPathDelayEvent clearPathDelayEvent = new ClearPathDelayEvent(system,rs.getInt(5),rs.getInt(6),pk,rs.getDouble(7));
			clearPathDelayEvents.add(clearPathDelayEvent);
		    System.out.printf("retrieved %s\n", clearPathDelayEvent);	
		}
		return clearPathDelayEvents;
	}

}
