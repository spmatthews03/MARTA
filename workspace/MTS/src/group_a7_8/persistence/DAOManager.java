package group_a7_8.persistence;

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

import edu.gatech.SimQueue;
import edu.gatech.TransitSystem;
import group_a7_8.FileProps;

@SuppressWarnings("rawtypes")
public class DAOManager {
	
	
	public static enum Table{
		//core entities
		BUS, BUSSTOP, BUSROUTE, DEPOT, RAILCAR, RAILROUTE, RAILSTATION, HAZARD, FUELCONSUMPTION
		//events
		,CLEARPATHDELAYEVENT,CLEARSPEEDLIMITEVENT,FACILITYOUTOFSERVICEEVENT,FACILITYRESUMESERVICEEVENT,MOVEBUSEVENT,
		MOVETRAINEVENT,SETPATHDELAYEVENT,SETSPEEDLIMITEVENT,VEHICLEOUTOFSERVICEEVENT,VEHICLERESUMESERVICEEVENT
		,BLOCKPATHEVENT, ROUTEDEFINITION
		};
	
    //Private
    private Connection con;
    PGSimpleDataSource ds = new PGSimpleDataSource();
	// Create default  connection parameters  
    private String connectionUrl = "jdbc:postgresql://localhost:5432/martadb";
    private String userId = "mts";
    private String password = "mts";
	private TransitSystem system;
	private SimQueue eventQueue;

    public DAOManager(TransitSystem system,SimQueue eventQueue) {
    	if(FileProps.contains("connectionUrl")) connectionUrl = FileProps.get("connectionUrl");
    	if(FileProps.contains("userId")) userId = FileProps.get("userId");
    	if(FileProps.contains("password")) password = FileProps.get("password");
        ds.setURL(connectionUrl);
        ds.setUser(userId);
        ds.setPassword(password);
        this.system = system;
        this.eventQueue = eventQueue;
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
    	File sql = new File(FileProps.get("createdb"));
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
    		System.out.printf("dropping %s\n", t.toString());
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
            case BUS: daoCache.put(t, new BusDAO(system,eventQueue,con));
            break;
            case BUSSTOP: daoCache.put(t, new BusStopDAO(system,eventQueue,this.con));
            break;
            case BUSROUTE: daoCache.put(t, new BusRouteDAO(system,eventQueue,this.con));
            break;
            case RAILCAR: daoCache.put(t, new RailCarDAO(system,eventQueue,this.con));
            break;
            case RAILROUTE: daoCache.put(t, new RailRouteDAO(system,eventQueue,this.con));
            break;
            case RAILSTATION: daoCache.put(t, new RailStationDAO(system,eventQueue,this.con));
            break;
            case DEPOT: daoCache.put(t, new DepotDAO(system,eventQueue,this.con));
            break;
            case HAZARD: daoCache.put(t, new HazardDAO(system,eventQueue,this.con));
            break;
            case FUELCONSUMPTION: daoCache.put(t, new FuelConsumptionDAO(system,eventQueue,this.con));
            break;
            case ROUTEDEFINITION: daoCache.put(t, new RouteDefinitionDAO(system,eventQueue,this.con));
            break;

            //events
            case CLEARPATHDELAYEVENT: daoCache.put(t, new ClearPathDelayEventDAO(system,eventQueue,this.con));
            break;
            case CLEARSPEEDLIMITEVENT: daoCache.put(t, new ClearSpeedLimitEventDAO(system,eventQueue,this.con));
            break;
            case FACILITYOUTOFSERVICEEVENT: daoCache.put(t, new FacilityOutOfServiceEventDAO(system,eventQueue,this.con));
            break;
            case FACILITYRESUMESERVICEEVENT: daoCache.put(t, new FacilityResumeServiceEventDAO(system,eventQueue,this.con));
            break;
            case MOVEBUSEVENT: daoCache.put(t, new MoveBusEventDAO(system,eventQueue,this.con));
            break;
            case MOVETRAINEVENT: daoCache.put(t, new MoveTrainEventDAO(system,eventQueue,this.con));
            break;
            case SETPATHDELAYEVENT: daoCache.put(t, new SetPathDelayEventDAO(system,eventQueue,this.con));
            break;
            case SETSPEEDLIMITEVENT: daoCache.put(t, new SetSpeedLimitEventDAO(system,eventQueue,this.con));
            break;
            case VEHICLEOUTOFSERVICEEVENT: daoCache.put(t, new VehicleOutOfServiceEventDAO(system,eventQueue,this.con));
            break;
            case VEHICLERESUMESERVICEEVENT: daoCache.put(t, new VehicleResumeServiceEventDAO(system,eventQueue,this.con));
            break;
            case BLOCKPATHEVENT: daoCache.put(t, new BlockPathEventDAO(system,eventQueue,this.con));
            break;
            default:
                throw new SQLException("Trying to link to an unexistant table.");
            }
    	}
    	return daoCache.get(t);

    }

    
    //make the DAOManager a singleton
    public static DAOManager INSTANCE;
    public static DAOManager getInstance(TransitSystem system,SimQueue eventQueue) throws Exception {
    	if (INSTANCE==null) {
    		INSTANCE = new DAOManager(system,eventQueue);
    	}
        return INSTANCE;
    }  

}