package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.event.FacilityOutOfServiceEvent;


public class FacilityOutOfServiceEventDAO extends GenericDAO<FacilityOutOfServiceEvent>{

	protected FacilityOutOfServiceEventDAO(Connection con) {
		super(con, "FACILITYOUTOFSERVICE", "type", "FacilityOutOfServiceEvent");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES(%s,'%s',%d,%s,%d)";

	@Override
	public void save(FacilityOutOfServiceEvent facilityOutOfServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"exchangePoint",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				facilityOutOfServiceEvent.getExchangePoint(),
				facilityOutOfServiceEvent.getOutOfService(),
				facilityOutOfServiceEvent.getRank(),
				facilityOutOfServiceEvent.getType(),
				facilityOutOfServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"exchangePoint",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				facilityOutOfServiceEvent.getExchangePoint(),
				facilityOutOfServiceEvent.getOutOfService(),
				facilityOutOfServiceEvent.getRank(),
				facilityOutOfServiceEvent.getType(),
				facilityOutOfServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FacilityOutOfServiceEvent> find() throws SQLException {
		ArrayList<FacilityOutOfServiceEvent> facilityOutOfServiceEvents = new ArrayList<FacilityOutOfServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"exchangePoint",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","FacilityOutOfServiceEvent"));
		while(rs.next()) {
			FacilityOutOfServiceEvent facilityOutOfServiceEvent= new FacilityOutOfServiceEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null);
			facilityOutOfServiceEvents.add(facilityOutOfServiceEvent);
		   System.out.printf("retrieved %s\n", facilityOutOfServiceEvents);
		}
		return facilityOutOfServiceEvents;
	}

}
