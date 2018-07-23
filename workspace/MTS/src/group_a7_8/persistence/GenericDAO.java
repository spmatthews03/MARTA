package group_a7_8.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import edu.gatech.Facility;
import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;

public abstract class GenericDAO<T> {
	protected static final String count_query_format ="SELECT COUNT(id) FROM %s WHERE %s='%s'";
	public int count() throws SQLException{
		PreparedStatement pstmt = con.prepareStatement(String.format(count_query_format,tableName,filterName,filterValue));
		ResultSet rs = pstmt.executeQuery();
		int count = 0;
	    if(rs.next()) {
	      count = rs.getInt(1);
	    }
	    rs.close();
	    pstmt.close();		
		return count;
	}
	protected Hashtable<String,Object>fields = new Hashtable<String,Object>();

    private String drop_stmt_format="DROP TABLE IF EXISTS %s";
    public void drop() throws SQLException {
		Statement stmt = con.createStatement();
   		stmt.execute(String.format(drop_stmt_format,tableName));
   		stmt.close();
    }

    private String truncate_stmt_format="TRUNCATE TABLE %s;";
    public void clear() throws SQLException {
		Statement stmt = con.createStatement();
   		stmt.execute(String.format(truncate_stmt_format,tableName));
   		stmt.close();
    }
    
    //Protected
    protected final String tableName;
    protected final String filterName;
    protected final String filterValue;
    protected Connection con;
	protected TransitSystem system;
	protected SimQueue eventQueue;

    protected GenericDAO(TransitSystem system,SimQueue eventQueue,Connection con, String tableName, String filterName, String filterValue) {
        this.tableName = tableName;
        this.filterName = filterName;
        this.filterValue = filterValue;
        this.con = con;
        this.system = system;
        this.eventQueue=eventQueue;
    }
    
    abstract public void save(T t) throws SQLException; 
    
    abstract public ArrayList<T> find() throws SQLException;
	protected Facility getFacility(String type, int id) {
		switch(type) {
		case "busStop":
			return system.getBusStop(id);
		case "railStop":
			return system.getRailStation(id);
		case "depot":
			return system.getDepot();
		}
		return null;
	}

}
