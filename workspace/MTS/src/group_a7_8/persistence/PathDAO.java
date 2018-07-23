package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.Path;
import group_a7_8.PathKey;


public class PathDAO extends GenericDAO<Path>{

	protected PathDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "PATH", "type", "path");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s %s) "+
			"VALUES(%d,%d,'%s','%s',%f,'%s','%d','%s')";

	@Override
	public void save(Path path) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"speedLimit",
				"isBlocked",
				"deltaStallDuration",
				"type",
				path.getPathKey().getOrigin().get_uniqueID(),
				path.getPathKey().getDestination().get_uniqueID(),
				path.getPathKey().getOrigin().getType(),
				path.getPathKey().getDestination().getType(),
				path.getSpeedLimit(),
				path.getIsBlocked(),
				path.get_delta_stall_duration(),
				filterValue
				));
		
		stmt.execute(String.format(insert_format,tableName,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"speedLimit",
				"isBlocked",
				"deltaStallDuration",
				"type",
				path.getPathKey().getOrigin().get_uniqueID(),
				path.getPathKey().getDestination().get_uniqueID(),
				path.getPathKey().getOrigin().getType(),
				path.getPathKey().getDestination().getType(),
				path.getSpeedLimit(),
				path.getIsBlocked(),
				path.get_delta_stall_duration(),
				filterValue
				));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<Path> find() throws SQLException {
		ArrayList<Path> paths = new ArrayList<Path>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"originID",
				"destinationID",
				"originType",
				"destinationType",
				"speedLimit",
				"isBlocked",
				"deltaStallDuration",
				tableName,"type",filterValue));
		while(rs.next()) {
			Facility origin = getFacility(rs.getString(3).trim(),rs.getInt(1));
			Facility destination = getFacility(rs.getString(4).trim(),rs.getInt(2));
			Double speedLimit = rs.getDouble(5);
			boolean isBlocked = rs.getBoolean(6);
			int delta_stall_duration = rs.getInt(7);
			PathKey pk = new PathKey(origin, destination);

			Path path = new Path(system, pk);
			if (isBlocked) {
				path.setIsBlocked();
			}
			path.set_delta_stall_duration(delta_stall_duration);
			path.setSpeedLimit(speedLimit);

		    paths.add(path);
		}
		return paths;
	}

}
