package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Depot;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;


public class DepotDAO extends GenericDAO<Depot>{

    protected DepotDAO(TransitSystem system, SimQueue eventQueue, Connection con) {
        super(system,eventQueue,con, "STOP", "type", "depot");
        System.out.printf("constructed %s\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s,%s) "+
                    "VALUES(%d,'%s',%f,%f,'%s')";


    @Override
    public void save(Depot depot) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "stopLogicalID",
                "name",
                "x",
                "y",
                "type",
                depot.get_uniqueID(),
                depot.getFacilityName(),
                depot.getLocation().getX(),
                depot.getLocation().getY(),
                filterValue));

        stmt.execute(String.format(insert_format,tableName,
                "stopLogicalID",
                "name",
                "x",
                "y",
                "type",
                depot.get_uniqueID(),
                depot.getFacilityName(),
                depot.getLocation().getX(),
                depot.getLocation().getY(),
                filterValue));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<Depot> find() throws SQLException {
        ArrayList<Depot> depots = new ArrayList<Depot>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "stopLogicalID",
                "name",
                "x",
                "y",
                tableName, "type", filterValue));
        while(rs.next()) {
            Depot depot = new Depot(
                    rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)
            );
            depots.add(depot);
            System.out.printf("retrieved %s\n", depot);
        }
        return depots;
    }

}
