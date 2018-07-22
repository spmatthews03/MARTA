package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Bus;
import edu.gatech.RailCar;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.event.MoveTrainEvent;


public class MoveTrainEventDAO extends GenericDAO<MoveTrainEvent>{

    protected MoveTrainEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
        super(system,eventQueue,con, "EVENT", "type", "move_train");
        System.out.printf(" constructed\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s,%s) "+
                    "VALUES('%s', %d, %d,'%s',%d)";


    @Override
    public void save(MoveTrainEvent moveTrainEvent) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                moveTrainEvent.get_current_train().getType(),
                moveTrainEvent.get_current_train().getID(),                
                moveTrainEvent.getRank(),
                filterValue,
                moveTrainEvent.getID()));

        stmt.execute(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                moveTrainEvent.get_current_train().getType(),
                moveTrainEvent.get_current_train().getID(),                
                moveTrainEvent.getRank(),
                filterValue,
                moveTrainEvent.getID()));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<MoveTrainEvent> find() throws SQLException {
        ArrayList<MoveTrainEvent> moveTrainEvents = new ArrayList<MoveTrainEvent>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "vehicleType",
                "vehicleID",
                "eventID",
                "timeRank",
                "type",
                tableName, "type", filterValue));
        while(rs.next()) {
        	RailCar train = system.getTrain(rs.getInt(2));
            MoveTrainEvent moveTrainEvent = new MoveTrainEvent(system,rs.getInt(3),rs.getInt(4),train);
            moveTrainEvents.add(moveTrainEvent);
            System.out.printf("retrieved %s\n", moveTrainEvent);
        }
        return moveTrainEvents;
    }

}
