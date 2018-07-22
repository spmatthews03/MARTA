package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class MoveTrainEventDAO extends GenericDAO<MoveTrainEvent>{

    protected MoveTrainEventDAO(Connection con) {
        super(con, "MOVETRAINEVENT", "type", "MoveTrainEvent");
        System.out.printf(" constructed\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s) "+
                    "VALUES(%s,%d,%s,%d)";


    @Override
    public void save(MoveTrainEvent moveTrainEvent) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "train",
                "timeRank",
                "eventType",
                "eventID",
                moveTrainEvent.get_current_train(),
                moveTrainEvent.getRank(),
                moveTrainEvent.getType(),
                moveTrainEvent.getID()));

        stmt.execute(String.format(insert_format,tableName,
                "train",
                "timeRank",
                "eventType",
                "eventID",
                moveTrainEvent.get_current_train(),
                moveTrainEvent.getRank(),
                moveTrainEvent.getType(),
                moveTrainEvent.getID()));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<MoveTrainEvent> find() throws SQLException {
        ArrayList<MoveTrainEvent> moveTrainEvents = new ArrayList<MoveTrainEvent>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "train",
                "timeRank",
                "eventType",
                "eventID",
                tableName, "type", "moveTrainEvent"));
        while(rs.next()) {
            MoveTrainEvent moveTrainEvent = new MoveTrainEvent(
                    null,
                    rs.getInt(1),
                    rs.getInt(2),
                    null);
            moveTrainEvents.add(moveTrainEvent);
            System.out.printf("retrieved %s\n", moveTrainEvent);
        }
        return moveTrainEvents;
    }

}
