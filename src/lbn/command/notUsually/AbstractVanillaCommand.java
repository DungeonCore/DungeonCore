package lbn.command.notUsually;

import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.CommandException;
import net.minecraft.server.v1_8_R1.ExceptionUsage;
import net.minecraft.server.v1_8_R1.ICommandListener;

public abstract class AbstractVanillaCommand extends AbstractNotUsuallyCommand{

	protected CommandAbstract command;
	public AbstractVanillaCommand(CommandAbstract command) {
		this.command = command;
	}

	@Override
	final public void execute(ICommandListener paramICommandListener,
			String[] paramArrayOfString) throws ExceptionUsage,
			CommandException {
			execute2(paramICommandListener,paramArrayOfString);
	}

	abstract protected void execute2(ICommandListener paramICommandListener,
			String[] paramArrayOfString) throws ExceptionUsage,	CommandException;

	@Override
	public int a() {
		return command.a();
	}

	@Override
	public String getCommand() {
		return command.getCommand();
	}

	@Override
	public String getUsage(ICommandListener paramICommandListener) {
		return command.getUsage(paramICommandListener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(Object o) {
		return command.compareTo(o);
	}
}
