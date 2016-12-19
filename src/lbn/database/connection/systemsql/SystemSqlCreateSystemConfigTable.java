package lbn.database.connection.systemsql;

public class SystemSqlCreateSystemConfigTable extends SystemSqlRunnable{

	@Override
	int getExecuteId() {
		return 1;
	}

	@Override
	String getSql() {
		return "create table chest_config ( "
				+ "last_chest_point_x int, "
				+ "last_chest_point_y int, "
				+ "last_chest_point_z int "
				+ " )"
				;
	}

}
