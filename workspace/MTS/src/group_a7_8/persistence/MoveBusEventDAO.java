package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Bus;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.event.MoveBusEvent;


public class MoveBusEventDAO extends GenericDAO<MoveBusEvent>{

    protected MoveBusEventDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
        super(system,eventQueue,con, "EVENT", "type", "move_bus");
        System.out.printf(" constructed\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s,%s) "+
                    "VALUES('%s', %d, %d,'%s',%d)";


    @Override
    public void save(MoveBusEvent moveBusEvent) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                moveBusEvent.getBus().getType(),
                moveBusEvent.getBus().getID(),                
                moveBusEvent.getRank(),
                filterValue,
                moveBusEvent.getID()));

        stmt.execute(String.format(insert_format,tableName,
                "vehicleType",
                "vehicleID",
                "timeRank",
                "type",
                "eventID",
                moveBusEvent.getBus().getType(),
                moveBusEvent.getBus().getID(),                
                moveBusEvent.getRank(),
                filterValue,
                moveBusEvent.getID()));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<MoveBusEvent> find() throws SQLException {
        ArrayList<MoveBusEvent> moveBusEvents = new ArrayList<MoveBusEvent>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "vehicleType",
                "vehicleID",
                "eventID",
                "timeRank",
                "type",
                tableName, "type", filterValue));
        while(rs.next()) {
        	Bus bus = system.getBus(rs.getInt(2));
            MoveBusEvent moveBusEvent = new MoveBusEvent(system,rs.getInt(3),rs.getInt(4),bus);
            moveBusEvents.add(moveBusEvent);
            System.out.printf("retrieved %s\n", moveBusEvent);
        }
        return moveBusEvents;
    }

}
