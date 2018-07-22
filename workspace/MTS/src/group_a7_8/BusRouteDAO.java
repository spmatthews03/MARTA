package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.BusRoute;


public class BusRouteDAO  extends GenericDAO<BusRoute>{
	protected BusRouteDAO(Connection con) {
		super(con, "ROUTE", "type", "BusRoute");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s) "+
			"VALUES(%d,'%s','%s',%d)";	

	@Override
	public void save(BusRoute route) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"routeLogicalID","type","name","routenumber",
				route.getID(),route.getType(),route.getName(),route.getNumber()));
		stmt.execute(String.format(insert_format,tableName,
				"routeLogicalID","type","name","routenumber",
				route.getID(),route.getType(),route.getName(),route.getNumber()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<BusRoute> find() throws SQLException {
		ArrayList<BusRoute> routes = new ArrayList<BusRoute>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"routeLogicalID","name","routenumber",
				tableName,"type","busroute"));
		while(rs.next()) {
		   BusRoute route = new BusRoute(rs.getInt(1),rs.getInt(3),rs.getString(2));
		   routes.add(route);
		   System.out.printf("retrieved %s\n", route);
		}
		return routes;
	}
	
}
