package main.lbn.SpletSheet;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface SheetRunnable<T> extends Callable<T> {
	void onCallbackFunction(Future<T> submit) throws Exception;

	boolean isTransaction();

	void closeTransaction();
}
