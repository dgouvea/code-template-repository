package coderepository.command;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import coderepository.Config;
import coderepository.command.Options.Args;

public abstract class AbstractCommandLine {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	protected Config config;
	
	public final void execute(Args args) {
		if (canExecute(args)) {
			try {
				logger.debug("Running command with args: " + args);
				run(args);
			} catch (Throwable e) {
				logger.error(e.getMessage(), e);
				log(e.getMessage());
			}
		}
	}
	
	public abstract Options options();

	protected final void log(String text) {
		System.out.println(text);
	}
	
	protected boolean canExecute(Args args) {
		return args.param(getCommand()).isPresent() && args.param(getCommand()).get().equals(getCommand());
	}
	
	protected void load(List<AbstractCommandLine> commands) {
		
	}
	
	protected abstract String getCommand();

	protected abstract String getHelp();
	
	protected abstract void run(Args args);

	
}
