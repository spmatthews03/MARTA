package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.Path;


public class PathDAO extends GenericDAO<Path>{

	protected PathDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "PATH", "type", "Path");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s) "+
			"VALUES(%d,'%s')";

	@Override
	public void save(Path path) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"speedLimit",
				"isBlocked",
				path.getSpeedLimit(),
				path.getIsBlocked()));
		
		stmt.execute(String.format(insert_format,tableName,
				"speedLimit",
				"isBlocked",
				path.getSpeedLimit(),
				path.getIsBlocked()));
		stmt.close();
	}

	private String select_format="select %s,%s from %s where %s='%s'";

	@Override
	public ArrayList<Path> find() throws SQLException {
		ArrayList<Path> paths = new ArrayList<Path>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"speedLimit",
				"isBlocked",
				tableName,"type","path"));
		while(rs.next()) {
			Path path = new Path(null,
					   null,
					   null);
		   paths.add(path);
		   System.out.printf("retrieved %s\n", path);
		}
		return paths;
	}

}
