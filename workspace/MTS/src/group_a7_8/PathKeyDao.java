package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class PathKeyDao extends GenericDAO<PathKey>{

	protected PathKeyDao(Connection con) {
		super(con, "PATHKEY", "type", "PathKey");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s) "+
			"VALUES(%s,'%s')";

	@Override
	public void save(PathKey pathKey) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"origin",
				"destination",
				pathKey.getOrigin(),
				pathKey.getDestination()));
		
		stmt.execute(String.format(insert_format,tableName,
				"origin",
				"destination",
				pathKey.getOrigin(),
				pathKey.getDestination()));
		stmt.close();
	}

	private String select_format="select %s,%s from %s where %s='%s'";

	@Override
	public ArrayList<PathKey> find() throws SQLException {
		ArrayList<PathKey> pathKeys = new ArrayList<PathKey>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"origin",
				"destination",
				tableName,"type","pathKey"));
		while(rs.next()) {
			PathKey pathKey = new PathKey(null,
					   null);
			pathKeys.add(pathKey);
		   System.out.printf("retrieved %s\n", pathKey);
		}
		return pathKeys;
	}

}
