package lbn.database.connection.systemsql;

public class SystemSqlCreateLastSaveTypeTable extends SystemSqlRunnable{

	@Override
	int getExecuteId() {
		return 3;
	}

	@Override
	String getSql() {
		return "create table last_save_type ( "
				+ "uuid char(40), "
				+ "save_type char(10) "
				+ " )"
				;
	}

}
