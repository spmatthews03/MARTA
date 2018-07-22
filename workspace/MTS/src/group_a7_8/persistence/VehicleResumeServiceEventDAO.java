package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import edu.gatech.Vehicle;
import group_a7_8.event.VehicleResumeServiceEvent;


public class VehicleResumeServiceEventDAO extends GenericDAO<VehicleResumeServiceEvent>{

	protected VehicleResumeServiceEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
		super(system,eventQueue,con, "EVENT", "type", "vehicle_resumed_service");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

    private String insert_format=
            "insert into %s (%s,%s,%s,%s,%s) "+
                    "VALUES('%s', %d, %d,'%s',%d)";

	@Override
	public void save(VehicleResumeServiceEvent vehicleResumeServiceEvent) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                vehicleResumeServiceEvent.getVehicle().getType(),
                vehicleResumeServiceEvent.getVehicle().getID(),                
                vehicleResumeServiceEvent.getRank(),
                filterValue,
                vehicleResumeServiceEvent.getID()));
		
		stmt.execute(String.format(insert_format,tableName,
				"vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                vehicleResumeServiceEvent.getVehicle().getType(),
                vehicleResumeServiceEvent.getVehicle().getID(),                
                vehicleResumeServiceEvent.getRank(),
                filterValue,
                vehicleResumeServiceEvent.getID()));
		stmt.close();
	}

    private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<VehicleResumeServiceEvent> find() throws SQLException {
		ArrayList<VehicleResumeServiceEvent> vehicleResumeServiceEvents = new ArrayList<VehicleResumeServiceEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
                "vehicleType",
                "vehicleID",
                "eventID",
                "timeRank",
                "type",
                tableName, "type", filterValue));
		while(rs.next()) {
			Vehicle vehicle = (rs.getString(1).equals("Bus")?system.getBus(rs.getInt(2)):system.getTrain(rs.getInt(2)));
			VehicleResumeServiceEvent vehicleResumeServiceEvent = new VehicleResumeServiceEvent(system,
					   rs.getInt(3),
					   rs.getInt(4),
					   vehicle);
			vehicleResumeServiceEvents.add(vehicleResumeServiceEvent);
		    System.out.printf("retrieved %s\n", vehicleResumeServiceEvent);
		}
		return vehicleResumeServiceEvents;
	}

}
