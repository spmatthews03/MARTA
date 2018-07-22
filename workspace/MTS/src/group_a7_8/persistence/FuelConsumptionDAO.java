package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.FuelConsumption;


public class FuelConsumptionDAO extends GenericDAO<FuelConsumption>{

	protected FuelConsumptionDAO(Connection con) {
		super(con, "FUELCONSUMPTION", "true", "true");
		System.out.printf("constructed %s\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s,%s) "+
			"VALUES(%d,%f,%d)";
	
	
	@Override
	public void save(FuelConsumption fuelConsumption) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"timeRank",
				"amount",
				"passengers",
				fuelConsumption.getTimeRank(),
				fuelConsumption.getFuelConsumed(),
				fuelConsumption.getPassengers()));
		
		stmt.execute(String.format(insert_format,tableName,
				"timeRank",
				"amount",
				"passengers",
				fuelConsumption.getTimeRank(),
				fuelConsumption.getFuelConsumed(),
				fuelConsumption.getPassengers()));
		stmt.close();
	}

	private String select_format="select %s,%s,%s from %s where %s='%s'";

	@Override
	public ArrayList<FuelConsumption> find() throws SQLException {
		ArrayList<FuelConsumption> fuelConsumptions = new ArrayList<FuelConsumption>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"timeRank",
				"amount",
				"passengers",
				tableName,"true","true"));
		while(rs.next()) {
			FuelConsumption fuelConsumption = new FuelConsumption(null, null, 
					   rs.getInt(1), rs.getDouble(2), rs.getInt(3));
			fuelConsumptions.add(fuelConsumption);
		   System.out.printf("retrieved %s\n", fuelConsumption);
		}
		return fuelConsumptions;
	}

}
