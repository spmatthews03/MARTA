package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.event.ClearSpeedLimitEvent;


public class ClearSpeedLimitEventDAO extends GenericDAO<ClearSpeedLimitEvent>{

	protected ClearSpeedLimitEventDAO(Connection con) {
		super(con, "CLEARSPEEDLIMIt", "type", "ClearSpeedLimit");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s) "+
			"VALUES(%s,%d,%s,%d)";

	@Override
	public void save(ClearSpeedLimitEvent clearSpeedLimitEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"pathKey",
				"timeRank",
				"eventType",
				"eventID",
				clearSpeedLimitEvent.getPathKey(),
				clearSpeedLimitEvent.getRank(),
				clearSpeedLimitEvent.getType(),
				clearSpeedLimitEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"pathKey",
				"timeRank",
				"eventType",
				"eventID",
				clearSpeedLimitEvent.getPathKey(),
				clearSpeedLimitEvent.getRank(),
				clearSpeedLimitEvent.getType(),
				clearSpeedLimitEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<ClearSpeedLimitEvent> find() throws SQLException {
		ArrayList<ClearSpeedLimitEvent> clearSpeedLimitEvents = new ArrayList<ClearSpeedLimitEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","ClearSpeedLimitEvent"));
		while(rs.next()) {
			ClearSpeedLimitEvent clearSpeedLimitEvent= new ClearSpeedLimitEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null);
			clearSpeedLimitEvents.add(clearSpeedLimitEvent);
		   System.out.printf("retrieved %s\n", clearSpeedLimitEvent);
		}
		return clearSpeedLimitEvents;
	}

}
