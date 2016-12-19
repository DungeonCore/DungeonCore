package main.database.connection.systemsql;

import java.util.ArrayList;
import java.util.Comparator;

import main.lbn.Main;

public class SystemSqlExecutor {
	static ArrayList<SystemSqlRunnable> runnableList = new ArrayList<SystemSqlRunnable>();
	static {
		runnableList.add(new SystemSqlCreateChestTable());
		runnableList.add(new SystemSqlCreateLastSaveTypeTable());
		runnableList.add(new SystemSqlCreatePlayerTable());
		runnableList.add(new SystemSqlCreateSystemConfigTable());

		runnableList.sort(new Comparator<SystemSqlRunnable>() {
			@Override
			public int compare(SystemSqlRunnable o1, SystemSqlRunnable o2) {
				return o1.getExecuteId() - o2.getExecuteId();
			}
		});
	}

	public static void execute() {
//		DatabaseConnection.makeSqlLiteFileIfNotExist();

		int executeId = Main.plugin.getConfig().getInt("systemsql");
		for (SystemSqlRunnable systemSqlRunnable : runnableList) {
			if (executeId < systemSqlRunnable.getExecuteId()) {
				systemSqlRunnable.execute();
			}
		}
	}

}
