package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.event.VehicleResumeServiceEvent;


public class VehicleResumeServiceEventDAO extends GenericDAO<VehicleResumeServiceEvent>{

	protected VehicleResumeServiceEventDAO(Connection con) {
		super(con, "VEHICLERESUMESERVICE", "type", "VehicleResumeServiceEvent");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s) "+
			"VALUES(%s,'%s',%d,%s,%d)";

	@Override
	public void save(VehicleResumeServiceEvent vehicleResumeServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"vehicle",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				vehicleResumeServiceEvent.getVehicle(),
				vehicleResumeServiceEvent.getOutOfService(),
				vehicleResumeServiceEvent.getRank(),
				vehicleResumeServiceEvent.getType(),
				vehicleResumeServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"vehicle",
				"outOfService",
				"timeRank",
				"eventType",
				"eventID",
				vehicleResumeServiceEvent.getVehicle(),
				vehicleResumeServiceEvent.getOutOfService(),
				vehicleResumeServiceEvent.getRank(),
				vehicleResumeServiceEvent.getType(),
				vehicleResumeServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<VehicleResumeServiceEvent> find() throws SQLException {
		ArrayList<VehicleResumeServiceEvent> vehicleResumeServiceEvents = new ArrayList<VehicleResumeServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","VehicleResumeServiceEvent"));
		while(rs.next()) {
			VehicleResumeServiceEvent vehicleResumeServiceEvent = new VehicleResumeServiceEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null);
			vehicleResumeServiceEvents.add(vehicleResumeServiceEvent);
		   System.out.printf("retrieved %s\n", vehicleResumeServiceEvent);
		}
		return vehicleResumeServiceEvents;
	}

}
