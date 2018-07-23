package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.RailRoute;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;


public class RailRouteDAO  extends GenericDAO<RailRoute>{
	protected RailRouteDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "ROUTE", "type", "railRoute");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s) "+
			"VALUES(%d,'%s','%s',%d)";	

	@Override
	public void save(RailRoute route) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"routeLogicalID","type","name","routenumber",
				route.getID(),filterValue,route.getName(),route.getNumber()));
		stmt.execute(String.format(insert_format,tableName,
				"routeLogicalID","type","name","routenumber",
				route.getID(),filterValue,route.getName(),route.getNumber()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<RailRoute> find() throws SQLException {
		ArrayList<RailRoute> routes = new ArrayList<RailRoute>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"routeLogicalID","name","routenumber",
				tableName,"type",filterValue));
		while(rs.next()) {
			RailRoute route = new RailRoute(rs.getInt(1),rs.getInt(3),rs.getString(2));
		   routes.add(route);
		   System.out.printf("retrieved %s\n", route);
		}
		return routes;
	}
	
}
