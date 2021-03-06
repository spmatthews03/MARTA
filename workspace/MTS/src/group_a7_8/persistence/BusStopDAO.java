package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.BusStop;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;


public class BusStopDAO  extends GenericDAO<BusStop>{
	protected BusStopDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system, eventQueue,con, "STOP", "type", "busStop");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s) "+
			"VALUES(%d,'%s',%f,%f,'%s',%d)";	
	@Override
	public void save(BusStop stop) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"stopLogicalID","type","x","y","name","waiting",
				stop.get_uniqueID(),filterValue,stop.getLocation().getX(),stop.getLocation().getY(),stop.getFacilityName(),stop.get_riders()));
		stmt.execute(String.format(insert_format,tableName,
				"stopLogicalID","type","x","y","name","waiting",
				stop.get_uniqueID(),filterValue,stop.getLocation().getX(),stop.getLocation().getY(),stop.getFacilityName(),stop.get_riders()));
		stmt.close();
	}
	
	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<BusStop> find() throws SQLException {
		ArrayList<BusStop> stops = new ArrayList<BusStop>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"stopLogicalID","name","waiting","x","y",
				tableName,"type",filterValue));
		while(rs.next()) {
			BusStop stop = new BusStop(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getDouble(4),rs.getDouble(5)); 
		   stops.add(stop);
		   System.out.printf("retrieved %s\n", stop);
		}
		return stops;
	}
	
}
