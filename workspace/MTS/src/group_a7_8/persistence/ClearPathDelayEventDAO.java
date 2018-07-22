package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class ClearPathDelayEventDAO extends GenericDAO<ClearPathDelayEvent>{

	protected ClearPathDelayEventDAO(Connection con) {
		super(con, "CLEARPATHDELAY", "type", "ClearPathDelay");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES(%s,%d,%d,%s,%d)";

	@Override
	public void save(ClearPathDelayEvent clearPathDelayEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"pathKey",
				"delayFactor",
				"timeRank",
				"eventType",
				"eventID,",
				clearPathDelayEvent.getPathKey(),
				clearPathDelayEvent.getDelayFactor(),
				clearPathDelayEvent.getRank(),
				clearPathDelayEvent.getType(),
				clearPathDelayEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"pathKey",
				"delayFactor",
				"timeRank",
				"eventType",
				"eventID,",
				clearPathDelayEvent.getPathKey(),
				clearPathDelayEvent.getDelayFactor(),
				clearPathDelayEvent.getRank(),
				clearPathDelayEvent.getType(),
				clearPathDelayEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<ClearPathDelayEvent> find() throws SQLException {
		ArrayList<ClearPathDelayEvent> clearPathDelayEvents = new ArrayList<ClearPathDelayEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"delayFactor",
				tableName,"type","ClearPathDelayEvent"));
		while(rs.next()) {
			ClearPathDelayEvent clearPathDelayEvent = new ClearPathDelayEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null,
					   rs.getDouble(3));
			clearPathDelayEvents.add(clearPathDelayEvent);
		   System.out.printf("retrieved %s\n", clearPathDelayEvent);
		}
		return clearPathDelayEvents;
	}

}
