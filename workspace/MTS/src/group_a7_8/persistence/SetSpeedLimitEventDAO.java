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
import group_a7_8.event.SetSpeedLimitEvent;


public class SetSpeedLimitEventDAO extends GenericDAO<SetSpeedLimitEvent>{

	protected SetSpeedLimitEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "set_speed_limit");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s) "+
					"VALUES('%s',%d,'%s',%d,%f,%d,'%s',%d)";

	@Override
	public void save(SetSpeedLimitEvent speedLimitEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"speedLimit",
				"timeRank",
				"type",
				"eventID",
				speedLimitEvent.getPathKey().getOrigin().getType(),
				speedLimitEvent.getPathKey().getOrigin().get_uniqueID(),
				speedLimitEvent.getPathKey().getDestination().getType(),
				speedLimitEvent.getPathKey().getDestination().get_uniqueID(),
				speedLimitEvent.getSpeedLimit(),
				speedLimitEvent.getRank(),
				filterValue,
				speedLimitEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"speedLimit",
				"timeRank",
				"type",
				"eventID",
				speedLimitEvent.getPathKey().getOrigin().getType(),
				speedLimitEvent.getPathKey().getOrigin().get_uniqueID(),
				speedLimitEvent.getPathKey().getDestination().getType(),
				speedLimitEvent.getPathKey().getDestination().get_uniqueID(),
				speedLimitEvent.getSpeedLimit(),
				speedLimitEvent.getRank(),
				filterValue,
				speedLimitEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<SetSpeedLimitEvent> find() throws SQLException {
		ArrayList<SetSpeedLimitEvent> speedLimitEvents = new ArrayList<SetSpeedLimitEvent>();
		Statement stmt = con.createStatement();
		System.out.printf("%s\n",String.format(select_format,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"eventID",
				"timeRank",
				"speedLimit",
				"type",
				tableName,"type",filterValue));
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"eventID",
				"timeRank",
				"speedLimit",
				"type",
				tableName,"type",filterValue));
		while(rs.next()) {
			Facility origin = getFacility(rs.getString(1),rs.getInt(2));
			Facility destination = getFacility(rs.getString(3),rs.getInt(4));
			PathKey pk = new PathKey(origin, destination);


			SetSpeedLimitEvent speedLimitEvent= new SetSpeedLimitEvent(system,rs.getInt(5),rs.getInt(6),pk,rs.getInt(7));
			speedLimitEvents.add(speedLimitEvent);
		    System.out.printf("retrieved %s\n", speedLimitEvent);
		}
		return speedLimitEvents;
	}

}
