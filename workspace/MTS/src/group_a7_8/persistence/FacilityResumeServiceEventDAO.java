package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class FacilityResumeServiceEventDAO extends GenericDAO<FacilityResumeServiceEvent>{

	protected FacilityResumeServiceEventDAO(Connection con) {
		super(con, "FACILITYRESUMESERVICE", "type", "FacilityResumeServiceEvent");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES(%s,'%s',%d,%s,%d)";

	@Override
	public void save(FacilityResumeServiceEvent facilityResumeServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"exchangePoint",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				facilityResumeServiceEvent.getExchangePoint(),
				facilityResumeServiceEvent.getOutOfService(),
				facilityResumeServiceEvent.getRank(),
				facilityResumeServiceEvent.getType(),
				facilityResumeServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"exchangePoint",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				facilityResumeServiceEvent.getExchangePoint(),
				facilityResumeServiceEvent.getOutOfService(),
				facilityResumeServiceEvent.getRank(),
				facilityResumeServiceEvent.getType(),
				facilityResumeServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FacilityResumeServiceEvent> find() throws SQLException {
		ArrayList<FacilityResumeServiceEvent> facilityResumeServiceEvents = new ArrayList<FacilityResumeServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","FacilityResumeServiceEvent"));
		while(rs.next()) {
			FacilityResumeServiceEvent facilityResumeServiceEvent = new FacilityResumeServiceEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null);
			facilityResumeServiceEvents.add(facilityResumeServiceEvent);
		   System.out.printf("retrieved %s\n", facilityResumeServiceEvent);
		}
		return facilityResumeServiceEvents;
	}

}
