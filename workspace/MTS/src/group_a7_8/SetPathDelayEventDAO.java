package group_a7_8;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SetPathDelayEventDAO extends GenericDAO<SetPathDelayEvent>{

	protected SetPathDelayEventDAO(Connection con) {
		super(con, "PATHDELAY", "type", "PathDelay");
		System.out.printf(" constructed\n",this.getClass().getSimpleName());
	}

	private String insert_format=
			"insert into %s (%s,%s) "+
			"VALUES(%s,'%s')";

	@Override
	public void save(SetPathDelayEvent pathDelay) throws SQLException {
		Statement stmt = con.createStatement();
		System.out.println(String.format(insert_format,tableName,
				"pathKey",
				"delayFactor",
				pathDelay.getPathKey(),
				pathDelay.getDelayFactor()));
		
		stmt.execute(String.format(insert_format,tableName,
				"pathKey",
				"delayFactor",
				pathDelay.getPathKey(),
				pathDelay.getDelayFactor()));
		stmt.close();
	}

	private String select_format="select %s,%s from %s where %s='%s'";

	@Override
	public ArrayList<SetPathDelayEvent> find() throws SQLException {
		ArrayList<SetPathDelayEvent> pathDelays = new ArrayList<SetPathDelayEvent>();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(String.format(select_format,
				"pathKey",
				"delayFactor",
				tableName,"type","SetPathDelayEvent"));
		while(rs.next()) {
			SetPathDelayEvent pathDelay = new SetPathDelayEvent(null,
					   rs.getInt(1),
					   rs.getInt(2),
					   null,
					   rs.getDouble(3));
			pathDelays.add(pathDelay);
		   System.out.printf("retrieved %s\n", pathDelay);
		}
		return pathDelays;
	}

}
