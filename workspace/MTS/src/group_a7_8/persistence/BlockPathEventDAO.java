package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.RailCar;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.Path;
import group_a7_8.PathKey;
import group_a7_8.event.BlockPathEvent;


public class BlockPathEventDAO extends GenericDAO<BlockPathEvent>{

	protected BlockPathEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "set_path_block");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s,%s) " +
			"VALUES('%s',%d,'%s',%d,'%s',%d,%d,%d,'%s')";

	@Override
	public void save(BlockPathEvent blockPathEvent) throws SQLException {
		Statement stmt = con.createStatement();
		
		Path path = blockPathEvent.getPath();
		System.out.println(String.format(insert_format,tableName,
				/* Path object */
				"originType",
				"originID",
				"destinationType",
				"destinationID",

				/* Train object */
				"vehicleType",
				"vehicleID",

				/* Event data */
				"eventID",
				"timeRank",
				"type",

				/* Path object */
				path.getOrigin().getType(),
				path.getOrigin().get_uniqueID(),
				path.getDestination().getType(),
				path.getDestination().get_uniqueID(),
				
				/* Train object */		
				blockPathEvent.getTrain().getType(),
				blockPathEvent.getTrain().getID(),

				/* Event data */
				blockPathEvent.getID(),
				blockPathEvent.getRank(),

				filterValue
				));
		
		stmt.execute(String.format(insert_format,tableName,
				/* Path object */
				"originType",
				"originID",
				"destinationType",
				"destinationID",

				/* Train object */
				"vehicleType",
				"vehicleID",

				/* Event data */
				"eventID",
				"timeRank",
				"type",

				/* Path object */
				path.getOrigin().getType(),
				path.getOrigin().get_uniqueID(),
				path.getDestination().getType(),
				path.getDestination().get_uniqueID(),
				
				/* Train object */		
				blockPathEvent.getTrain().getType(),
				blockPathEvent.getTrain().getID(),

				/* Event data */
				blockPathEvent.getID(),
				blockPathEvent.getRank(),

				filterValue
				));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<BlockPathEvent> find() throws SQLException {
		ArrayList<BlockPathEvent> blockPathEvents = new ArrayList<BlockPathEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				/* Path object */
				"originType",
				"originID",
				"destinationType",
				"destinationID",

				/* Train object */
				"vehicleType",
				"vehicleID",

				/* Event data */
				"eventID",
				"timeRank",
				"type",
				tableName,"type",filterValue));

		while(rs.next()) {
			/* Path object */
			String originType		= rs.getString(1);
			int originID			= rs.getInt(2);
			String destinationType	= rs.getString(3);
			int destinationID		= rs.getInt(4);
			
			Facility origin		 = getFacility(originType, originID);
			Facility destination = getFacility(destinationType, destinationID);
			PathKey path_key	 = new PathKey(origin, destination);
			Path    path		 = this.system.getPath(path_key);

			/* Train object */
			int trainID		= rs.getInt(6);
			RailCar train	= system.getTrain(trainID);
			
			/* Event data */
			Integer eventID  = rs.getInt(6);
			Integer timeRank = rs.getInt(7);

			BlockPathEvent blockPathEvent =
					new BlockPathEvent(system, eventID, timeRank, train, path);

			blockPathEvents.add(blockPathEvent);
		   System.out.printf("retrieved %s\n", blockPathEvent);
		}

		return blockPathEvents;
	}
}