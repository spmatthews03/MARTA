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
import group_a7_8.event.SetPathDelayEvent;


public class SetPathDelayEventDAO extends GenericDAO<SetPathDelayEvent>{

	protected SetPathDelayEventDAO(TransitSystem system, SimQueue eventQueue,Connection con) {
		super(system,eventQueue, con, "EVENT", "type", "set_path_delay");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,'%s',%d,%f,%d,'%s',%d)";

	@Override
	public void save(SetPathDelayEvent pathDelay) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"delayFactor",
				"timeRank",
				"type",
				"eventID,",
				pathDelay.getPathKey().getOrigin().getType(),
				pathDelay.getPathKey().getOrigin().get_uniqueID(),
				pathDelay.getPathKey().getDestination().getType(),
				pathDelay.getPathKey().getDestination().get_uniqueID(),
				pathDelay.getDelayFactor(),
				pathDelay.getRank(),
				filterValue,
				pathDelay.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"originType",
				"originID",
				"destinationType",
				"destinationID",
				"delayFactor",
				"timeRank",
				"type",
				"eventID",
				pathDelay.getPathKey().getOrigin().getType(),
				pathDelay.getPathKey().getOrigin().get_uniqueID(),
				pathDelay.getPathKey().getDestination().getType(),
				pathDelay.getPathKey().getDestination().get_uniqueID(),
				pathDelay.getDelayFactor(),
				pathDelay.getRank(),
				filterValue,
				pathDelay.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<SetPathDelayEvent> find() throws SQLException {
		ArrayList<SetPathDelayEvent>pathDelays = new ArrayList<SetPathDelayEvent>();
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
			SetPathDelayEvent pathDelay = new SetPathDelayEvent(system,rs.getInt(5),rs.getInt(6),pk,rs.getDouble(7));
			pathDelays.add(pathDelay);
		   System.out.printf("retrieved %s\n", pathDelay);
		}
		return pathDelays;
	}

}
