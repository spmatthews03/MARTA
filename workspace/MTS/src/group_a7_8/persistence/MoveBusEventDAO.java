package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MoveBusEventDAO extends GenericDAO<MoveBusEvent>{

    protected MoveBusEventDAO(Connection con) {
        super(con, "MOVEBUSEVENT", "type", "MoveBusEvent");
        System.out.printf(" constructed\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s) "+
                    "VALUES(%s,%d,%s,%d)";


    @Override
    public void save(MoveBusEvent moveBusEvent) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "bus",
                "timeRank",
                "eventType",
                "eventID",
                moveBusEvent.getBus(),
                moveBusEvent.getRank(),
                moveBusEvent.getType(),
                moveBusEvent.getID()));

        stmt.execute(String.format(insert_format,tableName,
                "bus",
                "timeRank",
                "eventType",
                "eventID",
                moveBusEvent.getBus(),
                moveBusEvent.getRank(),
                moveBusEvent.getType(),
                moveBusEvent.getID()));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<MoveBusEvent> find() throws SQLException {
        ArrayList<MoveBusEvent> moveBusEvents = new ArrayList<MoveBusEvent>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "bus",
                "timeRank",
                "eventType",
                "eventID",
                tableName, "type", "moveBusEvent"));
        while(rs.next()) {
            MoveBusEvent moveBusEvent = new MoveBusEvent(
                    null,
                    rs.getInt(1),
                    rs.getInt(2),
                    null);
            moveBusEvents.add(moveBusEvent);
            System.out.printf("retrieved %s\n", moveBusEvent);
        }
        return moveBusEvents;
    }

}
