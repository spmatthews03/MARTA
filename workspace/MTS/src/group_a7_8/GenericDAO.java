package group_a7_8;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

public abstract class GenericDAO<T> {
	protected static final String count_query_format ="SELECT COUNT(id) FROM %s";
	public int count() throws SQLException{
		PreparedStatement pstmt = con.prepareStatement(String.format(count_query_format,tableName));
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
    protected Connection con;

    protected GenericDAO(Connection con, String tableName) {
        this.tableName = tableName;
        this.con = con;
    }
    
    abstract public void save(T t) throws SQLException; 
    
    abstract public ArrayList<T> find() throws SQLException;

}
