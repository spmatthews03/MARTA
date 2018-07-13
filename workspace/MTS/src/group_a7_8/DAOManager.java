package group_a7_8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.function.Consumer;

import org.postgresql.ds.PGSimpleDataSource;

@SuppressWarnings("rawtypes")
public class DAOManager {
	
	
	public static enum Table{BUS, BUSSTOP, BUSROUTE};
	
    //Private
    private Connection con;
    PGSimpleDataSource ds = new PGSimpleDataSource();
	// Create a variable for the connection string.  
    private String connectionUrl = "jdbc:postgresql://localhost:5432/martadb";
    private String userId = "mts";
    private String password = "mts";

    public DAOManager() throws Exception {
        ds.setURL(connectionUrl);
        ds.setUser(userId);
        ds.setPassword(password);
    }

    public void open() throws SQLException, ClassNotFoundException {
        try
        {
            if(this.con==null || this.con.isClosed()) {
                //this.con = src.getConnection();
            	// Establish the connection.  
            	con = ds.getConnection();
            }
            System.out.printf("db connection retrieved\n");
        }
        catch(SQLException e) { throw e; }
    }

    public void close() throws SQLException {
        try
        {
            if(this.con!=null && !this.con.isClosed())
                this.con.close();
        }
        catch(SQLException e) { throw e; }
    }
    
    //cache the concrete class doas
    private Hashtable<Table,GenericDAO> daoCache = new Hashtable<Table,GenericDAO>();
    public void clearDB() throws SQLException, ClassNotFoundException {
    	for(Table t : Table.values()) {
    		getDAO(t).clear();
    	}
    }

    public void createDB() throws SQLException, ClassNotFoundException, IOException {
    	File sql = new File("t:\\db\\createDB.sql");
    	if(sql.exists()&&sql.isFile()) {
    		System.out.print("found sql script file\n");
    	}
    	else {
    		System.out.print("sql script file not found\n");
    	}
        try
        {
            if(this.con == null || this.con.isClosed()) //Let's ensure our connection is open   
                this.open();
        }
        catch(SQLException e){ throw e; }
        
    	Statement stmt = con.createStatement();
    	BufferedReader br = new BufferedReader(new FileReader(sql));
    	StringBuilder command = new StringBuilder();
    	br.lines().forEachOrdered(new Consumer<String>() {

			@Override
			public void accept(String line) {
				line = line.trim();
				System.out.printf("%s\n", line);
				command.append(line);
				if(command.length()>0) command.append(' ');
				if(line.endsWith(";")){
					try {
						System.out.printf("executing: %s\n",command.toString());
						stmt.execute(command.toString());
						command.setLength(0);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
    	stmt.close();
    	br.close();
    }
    
    public void dropAll() throws SQLException, ClassNotFoundException {
    	for(Table t : Table.values()) {
    		getDAO(t).drop();
    	}
    }
    
    public GenericDAO getDAO(Table t) throws SQLException, ClassNotFoundException 
    {

        try
        {
            if(this.con == null || this.con.isClosed()) //Let's ensure our connection is open   
                this.open();
        }
        catch(SQLException e){ throw e; }
        
    	if(!daoCache.containsKey(t)) {
    		//lazily creates the daos we will need
            switch(t)
            {
            case BUS: daoCache.put(t, new BusDAO(con));
            break;
            case BUSSTOP: daoCache.put(t, new BusStopDAO(this.con));
            break;
            case BUSROUTE: daoCache.put(t, new BusRouteDAO(this.con));
            break;
            default:
                throw new SQLException("Trying to link to an unexistant table.");
            }
    	}
    	return daoCache.get(t);

    }

    
    //make the DAOManager a singleton
    public static DAOManager INSTANCE;
    public static DAOManager getInstance() throws Exception {
    	if (INSTANCE==null) {
    		INSTANCE = new DAOManager();
    	}
        return INSTANCE;
    }  

}