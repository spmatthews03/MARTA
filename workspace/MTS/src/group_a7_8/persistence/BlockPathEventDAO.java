package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.event.BlockPathEvent;


public class BlockPathEventDAO extends GenericDAO<BlockPathEvent>{

	protected BlockPathEventDAO(Connection con) {
		super(con, "BLOCKPATHEVENT", "type", "BlockPathEvent");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES(%s,%s,%s,%s,%s,%s,%d,%s,%d)";

	@Override
	public void save(BlockPathEvent blockPathEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"route",
				"origin",
				"destination",
				"pathKey",
				"path",
				"timeRank",
				"eventType",
				"eventID",
				blockPathEvent.getRoute(),
				blockPathEvent.getOrigin(),
				blockPathEvent.getDestination(),
				blockPathEvent.getPathKey(),
				blockPathEvent.getPath(),
				blockPathEvent.getRank(),
				blockPathEvent.getType(),
				blockPathEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"route",
				"origin",
				"destination",
				"pathKey",
				"path",
				"timeRank",
				"eventType",
				"eventID",
				blockPathEvent.getRoute(),
				blockPathEvent.getOrigin(),
				blockPathEvent.getDestination(),
				blockPathEvent.getPathKey(),
				blockPathEvent.getPath(),
				blockPathEvent.getRank(),
				blockPathEvent.getType(),
				blockPathEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<BlockPathEvent> find() throws SQLException {
		ArrayList<BlockPathEvent> blockPathEvents = new ArrayList<BlockPathEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"route",
				"origin",
				"destination",
				"pathKey",
				"path",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","BlockPathEvent"));
		while(rs.next()) {
			BlockPathEvent blockPathEvent = new BlockPathEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null);
			blockPathEvents.add(blockPathEvent);
		   System.out.printf("retrieved %s\n", blockPathEvent);
		}
		return blockPathEvents;
	}

}
