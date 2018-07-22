package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.ExchangePoint;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.event.FacilityOutOfServiceEvent;
import group_a7_8.event.FacilityResumeServiceEvent;


public class FacilityResumeServiceEventDAO extends GenericDAO<FacilityResumeServiceEvent>{

	protected FacilityResumeServiceEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "exchangePoint_resumed_service");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,%d,'%s',%d)";


	@Override
	public void save(FacilityResumeServiceEvent facilityResumeServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"exchangePointType",
				"exchangePointID",
				"timeRank",
				"type",
				"eventID",
				facilityResumeServiceEvent.getExchangePoint().getType(),
				facilityResumeServiceEvent.getExchangePoint().get_uniqueID(),
				facilityResumeServiceEvent.getRank(),
				filterValue,
				facilityResumeServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"exchangePointType",
				"exchangePointID",
				"timeRank",
				"type",
				"eventID",
				facilityResumeServiceEvent.getExchangePoint().getType(),
				facilityResumeServiceEvent.getExchangePoint().get_uniqueID(),
				facilityResumeServiceEvent.getRank(),
				filterValue,
				facilityResumeServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FacilityResumeServiceEvent> find() throws SQLException {
		ArrayList<FacilityResumeServiceEvent> facilityResumeServiceEvents = new ArrayList<FacilityResumeServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"exchangePointType",
				"exchangePointID",
				"eventID",
				"timeRank",
				"type",
				tableName,"type","FacilityResumeServiceEvent"));
		while(rs.next()) {
			ExchangePoint facility = (ExchangePoint)getFacility(rs.getString(1),rs.getInt(2));
			FacilityResumeServiceEvent facilityResumeServiceEvent= new FacilityResumeServiceEvent(system,rs.getInt(3),rs.getInt(4),facility);
			facilityResumeServiceEvents.add(facilityResumeServiceEvent);
		    System.out.printf("retrieved %s\n", facilityResumeServiceEvent);
		}
		return facilityResumeServiceEvents;
	}

}
