package main.database.connection.systemsql;

public class SystemSqlCreatePlayerTable extends SystemSqlRunnable{

	@Override
	int getExecuteId() {
		return 2;
	}

	@Override
	String getSql() {
		return "create table player ( "
				+ "uuid char(40), "
				+ "save_type char(10), "
				+ "sword_level int, "
				+ "sword_exp int, "
				+ "bow_level int, "
				+ "bow_exp int, "
				+ "magic_level int, "
				+ "magic_exp int, "
				+ "galions int "
				+ " )"
				;
	}

}
