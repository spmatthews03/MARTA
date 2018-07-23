package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import edu.gatech.VehicleRoute;
import group_a7_8.RouteDefinition;


public class RouteDefinitionDAO extends GenericDAO<RouteDefinition>{

	protected RouteDefinitionDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "ROUTEDEFINITION", "type", "routeDefinition");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,%d,'%s',%d,'%s')";

	@Override
	public void save(RouteDefinition routeDefinition) throws SQLException {
		Statement stmt = con.createStatement();
			System.out.println(String.format(insert_format,tableName,
					"routeType",
					"routeID",
					"seqno",
					"stopType",
					"stopID",
					"type",
					routeDefinition.getRoute().getType(),
					routeDefinition.getRoute().getID(),
					routeDefinition.getOrder(),
					routeDefinition.getFacility().getType(),
					routeDefinition.getFacility().get_uniqueID(),
					filterValue
					));
			
			stmt.execute(String.format(insert_format,tableName,
					"routeType",
					"routeID",
					"seqno",
					"stopType",
					"stopID",
					"type",
					routeDefinition.getRoute().getType(),
					routeDefinition.getRoute().getID(),
					routeDefinition.getOrder(),
					routeDefinition.getFacility().getType(),
					routeDefinition.getFacility().get_uniqueID(),
					filterValue
					));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s' order by %s asc, %s asc, %s asc";

	@Override
	public ArrayList<RouteDefinition> find() throws SQLException {
		ArrayList<RouteDefinition> def = new ArrayList<RouteDefinition>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"routeType",
				"routeID",
				"stopType",
				"stopID",
				"seqno",
				tableName,"type",filterValue,"routeType","routeID","seqno"));
		while(rs.next()) {
		    String routeType = rs.getString(1).trim();
		    VehicleRoute route = (routeType.equals("busRoute")?system.getBusRoute(rs.getInt(2)):system.getRailRoute(rs.getInt(2)));
		    String facilityType = rs.getString(3).trim();
		    Facility facility = (facilityType.equals("busStop")?system.getBusStop(rs.getInt(4)):system.getRailStation(rs.getInt(4)));
		    int order = rs.getInt(5);
		    RouteDefinition routeDefinition = new RouteDefinition(route, order, facility);
		   System.out.printf("retrieved %s\n", routeDefinition);
		   def.add(routeDefinition);
		}
		return def;
	}

}
