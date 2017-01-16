package lbn.command.notUsually;

import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.ICommandListener;

public abstract class AbstractVanillaCommand extends AbstractNotUsuallyCommand{

	protected CommandAbstract command;
	public AbstractVanillaCommand(CommandAbstract command) {
		this.command = command;
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
