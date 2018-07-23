package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.RailStation;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;


public class RailStationDAO  extends GenericDAO<RailStation>{
	protected RailStationDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "STOP", "type", "railStop");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,'%s',%f,%f,'%s',%d)";	
	@Override
	public void save(RailStation station) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"stopLogicalID",
				"type",
				"x",
				"y",
				"name",
				"waiting",
				station.get_uniqueID(),
				filterValue,
				station.getLocation().getX(),
				station.getLocation().getY(),
				station.getFacilityName(),
				station.get_riders()));
		stmt.execute(String.format(insert_format,tableName,
				"stopLogicalID",
				"type",
				"x",
				"y",
				"name",
				"waiting",
				station.get_uniqueID(),
				filterValue,
				station.getLocation().getX(),
				station.getLocation().getY(),
				station.getFacilityName(),
				station.get_riders()));
		stmt.close();
	}
	
	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<RailStation> find() throws SQLException {
		ArrayList<RailStation> stations = new ArrayList<RailStation>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"stopLogicalID","name","waiting","x","y",
				tableName,"type",filterValue));
		while(rs.next()) {
			RailStation station = new RailStation(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)); 
			stations.add(station);
		   System.out.printf("retrieved %s\n", station);
		}
		return stations;
	}
	
}
