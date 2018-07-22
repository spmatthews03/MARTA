package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class VehicleOutOfServiceEventDAO extends GenericDAO<VehicleOutOfServiceEvent>{

	protected VehicleOutOfServiceEventDAO(Connection con) {
		super(con, "VEHICLEOUTOFSERVICE", "type", "VehicleOutOfService");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES(%s,'%s',%d,%d,%d,%d,%s,%d)";

	@Override
	public void save(VehicleOutOfServiceEvent vehicleOutOfServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"vehicle",
				"outOfService",
				"stallDuration",
				"repairDuration",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				vehicleOutOfServiceEvent.getVehicle(),
				vehicleOutOfServiceEvent.getOutOfService(),
				vehicleOutOfServiceEvent.getStallDuration(),
				vehicleOutOfServiceEvent.getRepairDuration(),
				vehicleOutOfServiceEvent.getRank(),
				vehicleOutOfServiceEvent.getType(),
				vehicleOutOfServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"vehicle",
				"outOfService",
				"stallDuration",
				"repairDuration",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				vehicleOutOfServiceEvent.getVehicle(),
				vehicleOutOfServiceEvent.getOutOfService(),
				vehicleOutOfServiceEvent.getStallDuration(),
				vehicleOutOfServiceEvent.getRepairDuration(),
				vehicleOutOfServiceEvent.getRank(),
				vehicleOutOfServiceEvent.getType(),
				vehicleOutOfServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<VehicleOutOfServiceEvent> find() throws SQLException {
		ArrayList<VehicleOutOfServiceEvent> vehicleOutOfServiceEvents = new ArrayList<VehicleOutOfServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"vehicle",
				"outOfService",
				"stallDuration",
				"repairDuration",
				"speedLimit",
				"timeRank",
				"eventType",
				"eventID",
				tableName,"type","VehicleOutOfServiceEvent"));
		while(rs.next()) {
			VehicleOutOfServiceEvent vehicleOutOfServiceEvent= new VehicleOutOfServiceEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null,
					   rs.getInt(3),
					   rs.getInt(4));
			vehicleOutOfServiceEvents.add(vehicleOutOfServiceEvent);
		   System.out.printf("retrieved %s\n", vehicleOutOfServiceEvent);
		}
		return vehicleOutOfServiceEvents;
	}

}
