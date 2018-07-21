package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import group_a7_8.Hazard;


public class HazardDAO extends GenericDAO<Hazard>{

	protected HazardDAO(Connection con) {
		super(con, "HAZARD", "true", "true");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s) "+
			"VALUES(%d,%f)";
	
	
	@Override
	public void save(Hazard hazard) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"delayFactor",
				hazard.getDelayFactor()));
		
		stmt.execute(String.format(insert_format,tableName,
				"delayFactor",
				hazard.getDelayFactor()));
		stmt.close();
	}

	private String select_format="select %s,%s from %s where %s='%s'";

	@Override
	public ArrayList<Hazard> find() throws SQLException {
		ArrayList<Hazard> hazards = new ArrayList<Hazard>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"delayFactor",
				tableName,"true","true"));
		while(rs.next()) {
			Hazard hazard = new Hazard(null,
					   rs.getDouble(1));
			hazards.add(hazard);
		   System.out.printf("retrieved %s\n", hazard);
		}
		return hazards;
	}

}
