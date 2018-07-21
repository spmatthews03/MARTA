package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SetSpeedLimitEventDAO extends GenericDAO<SetSpeedLimitEvent>{

	protected SetSpeedLimitEventDAO(Connection con) {
		super(con, "SPEEDLIMIT", "type", "SpeedLimit");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES(%s,%d,%d,%s,%d)";

	@Override
	public void save(SetSpeedLimitEvent speedLimitEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"pathKey",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				speedLimitEvent.getPathKey(),
				speedLimitEvent.getSpeedLimit(),
				speedLimitEvent.getRank(),
				speedLimitEvent.getType(),
				speedLimitEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"pathKey",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				speedLimitEvent.getPathKey(),
				speedLimitEvent.getSpeedLimit(),
				speedLimitEvent.getRank(),
				speedLimitEvent.getType(),
				speedLimitEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<SetSpeedLimitEvent> find() throws SQLException {
		ArrayList<SetSpeedLimitEvent> speedLimitEvents = new ArrayList<SetSpeedLimitEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","SetSpeedLimitEvent"));
		while(rs.next()) {
			SetSpeedLimitEvent speedLimitEvent= new SetSpeedLimitEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null,
					   rs.getInt(3));
			speedLimitEvents.add(speedLimitEvent);
		   System.out.printf("retrieved %s\n", speedLimitEvent);
		}
		return speedLimitEvents;
	}

}
