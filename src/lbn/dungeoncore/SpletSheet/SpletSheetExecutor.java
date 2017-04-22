package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import lbn.util.LbnRunnable;

public class SpletSheetExecutor {
	static ExecutorService service = Executors.newFixedThreadPool(3);

	public static <T> void onExecute(SheetRunnable<T> run) {
		try {
			Future<T> submit = service.submit(run);
			new LbnRunnable() {
				@Override
				public void run2() {
					try {
						if (submit.isDone() || submit.isCancelled()) {
							run.onCallbackFunction(submit);
							cancel();
						}
					} catch (Exception e) {
						e.printStackTrace();
						run.closeTransaction();
						cancel();
					}
				}
			}.runTaskTimer(20);

		} catch (Exception e) {
			run.closeTransaction();
			e.printStackTrace();
		}
	}

}
