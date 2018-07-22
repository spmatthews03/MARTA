package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import edu.gatech.Depot;


public class DepotDAO extends GenericDAO<Depot>{

    protected DepotDAO(Connection con) {
        super(con, "STOP", "type", "Depot");
        System.out.printf("constructed %s\n",this.getClass().getSimpleName());
    }

    private String insert_format=
            "insert into %s (%s,%s,%s,%s) "+
                    "VALUES(%d,'%s',%f,%f)";


    @Override
    public void save(Depot depot) throws SQLException {
        Statement stmt = con.createStatement();
        System.out.printf(String.format(insert_format,tableName,
                "depotLogicalID",
                "name",
                "x",
                "y",
                depot.get_uniqueID(),
                depot.getFacilityName(),
                depot.getLocation().getX(),
                depot.getLocation().getY()));

        stmt.execute(String.format(insert_format,tableName,
                "depotLogicalID",
                "name",
                "x",
                "y",
                depot.get_uniqueID(),
                depot.getFacilityName(),
                depot.getLocation().getX(),
                depot.getLocation().getY()));
        stmt.close();
    }

    private String select_format="select %s,%s,%s,%s from %s where %s='%s'";

    @Override
    public ArrayList<Depot> find() throws SQLException {
        ArrayList<Depot> depots = new ArrayList<Depot>();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(String.format(select_format,
                "depotLogicalID",
                "name",
                "x",
                "y",
                tableName, "type", "depot"));
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
