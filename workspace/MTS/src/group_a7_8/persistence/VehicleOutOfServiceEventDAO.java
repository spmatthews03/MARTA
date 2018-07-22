package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import group_a7_8.event.VehicleOutOfServiceEvent;


public class VehicleOutOfServiceEventDAO extends GenericDAO<VehicleOutOfServiceEvent>{

	protected VehicleOutOfServiceEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "vehicle_out_of_service");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s,%s,%s,%s,%s) "+
			"VALUES('%s', %d, %d, %d, %d, '%s', %d)";

	@Override
	public void save(VehicleOutOfServiceEvent vehicleOutOfServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
				"deltaStallDuration",
				"repairDuration",
                "timeRank",
                "type",
                "eventID",
				vehicleOutOfServiceEvent.getVehicle().getType(),
				vehicleOutOfServiceEvent.getVehicle().getID(),
				vehicleOutOfServiceEvent.getStallDuration(),
				vehicleOutOfServiceEvent.getRepairDuration(),
				vehicleOutOfServiceEvent.getRank(),
				filterValue,
				vehicleOutOfServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
				"deltaStallDuration",
				"repairDuration",
                "timeRank",
                "type",
                "eventID",
				vehicleOutOfServiceEvent.getVehicle().getType(),
				vehicleOutOfServiceEvent.getVehicle().getID(),
				vehicleOutOfServiceEvent.getStallDuration(),
				vehicleOutOfServiceEvent.getRepairDuration(),
				vehicleOutOfServiceEvent.getRank(),
				filterValue,
				vehicleOutOfServiceEvent.getID()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<VehicleOutOfServiceEvent> find() throws SQLException {
		ArrayList<VehicleOutOfServiceEvent> vehicleOutOfServiceEvents = new ArrayList<VehicleOutOfServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
                "vehicleType",
                "vehicleID",
				"eventID",
				"timeRank",
				"deltaStallDuration",
				"repairDuration",
				"type",
				tableName,"type",filterValue));
		while(rs.next()) {
			Vehicle vehicle = (rs.getString(1).equals("Bus")?system.getBus(rs.getInt(2)):system.getTrain(rs.getInt(2)));
			VehicleOutOfServiceEvent vehicleOutOfServiceEvent= new VehicleOutOfServiceEvent(system,
					   rs.getInt(3),
					   rs.getInt(4),
					   vehicle,
					   rs.getInt(5),
					   rs.getInt(6));
			vehicleOutOfServiceEvents.add(vehicleOutOfServiceEvent);
		    System.out.printf("retrieved %s\n", vehicleOutOfServiceEvent);
		}
		return vehicleOutOfServiceEvents;
	}

}
