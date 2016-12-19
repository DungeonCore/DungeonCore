package main.database.connection.systemsql;

public class SystemSqlCreateChestTable extends SystemSqlRunnable{

	@Override
	int getExecuteId() {
		return 4;
	}

	@Override
	String getSql() {
		return "create table chest ( "
				+ "uuid char(40), "
				+ "chesttype char(10), "
				+ "x int(10), "
				+ "y int(10), "
				+ "z int(10) "
				+ " )"
				;
	}

}
