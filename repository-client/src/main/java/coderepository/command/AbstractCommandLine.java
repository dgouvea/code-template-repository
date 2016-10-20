package coderepository.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import coderepository.Config;

public abstract class AbstractCommandLine {

	@Autowired
	protected Config config;
	
	private final List<AbstractCommandLine> commands = new ArrayList<>();
	
	public final void execute(String action, Args args) {
		if (canExecute(action, args)) {
			try {
				run(action, args);
			} catch (Throwable e) {
				e.printStackTrace();
				log(e.getMessage());
			}
		}
	}

	protected final void log(String text) {
		System.out.println(text);
	}
	
	protected final void next(String action, Args args) {
		commands.forEach(command -> {
			command.execute(action, args);
		});
	}
	
	protected boolean canExecute(String action, Args args) {
		return getCommand().equals(action);
	}
	
	@PostConstruct
	private void load() {
		load(commands);
	}
	
	protected void load(List<AbstractCommandLine> commands) {
		
	}
	
	protected abstract String getCommand();

	protected abstract String getHelp();
	
	protected abstract void run(String action, Args args);

	
}
