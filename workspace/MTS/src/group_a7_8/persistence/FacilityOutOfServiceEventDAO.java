package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.ExchangePoint;
import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.event.FacilityOutOfServiceEvent;


public class FacilityOutOfServiceEventDAO extends GenericDAO<FacilityOutOfServiceEvent>{

	protected FacilityOutOfServiceEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue, con, "EVENT", "type", "exchangePoint_out_of_service");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES('%s',%d,%d,'%s',%d)";

	@Override
	public void save(FacilityOutOfServiceEvent facilityOutOfServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"exchangePointType",
				"exchangePointID",
				"timeRank",
				"type",
				"eventID",
				facilityOutOfServiceEvent.getExchangePoint().getType(),
				facilityOutOfServiceEvent.getExchangePoint().get_uniqueID(),
				facilityOutOfServiceEvent.getRank(),
				filterValue,
				facilityOutOfServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"exchangePointType",
				"exchangePointID",
				"timeRank",
				"type",
				"eventID",
				facilityOutOfServiceEvent.getExchangePoint().getType(),
				facilityOutOfServiceEvent.getExchangePoint().get_uniqueID(),
				facilityOutOfServiceEvent.getRank(),
				filterValue,
				facilityOutOfServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FacilityOutOfServiceEvent> find() throws SQLException {
		ArrayList<FacilityOutOfServiceEvent> facilityOutOfServiceEvents = new ArrayList<FacilityOutOfServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"exchangePointType",
				"exchangePointID",
				"eventID",
				"timeRank",
				"type",
				tableName,"type",filterValue));
		while(rs.next()) {
			ExchangePoint facility = (ExchangePoint)getFacility(rs.getString(1),rs.getInt(2));
			FacilityOutOfServiceEvent facilityOutOfServiceEvent= new FacilityOutOfServiceEvent(system,rs.getInt(3),rs.getInt(4),facility);
			facilityOutOfServiceEvents.add(facilityOutOfServiceEvent);
		    System.out.printf("retrieved %s\n", facilityOutOfServiceEvents);
		}
		return facilityOutOfServiceEvents;
	}

}
