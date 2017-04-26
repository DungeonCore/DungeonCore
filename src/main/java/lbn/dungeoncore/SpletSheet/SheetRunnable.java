package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface SheetRunnable<T> extends Callable<T> {
	public static final String THE_LOW_SHEET2 = "TheLow一般向け設定";

	void onCallbackFunction(Future<T> submit) throws Exception;

	boolean isTransaction();

	void closeTransaction();

	String getSheetName();

	String[] getTag();

	boolean hasSecoundSheet();
}
