package coderepository.command;

import org.springframework.stereotype.Component;

@Component
public class ConfigDefaultCommand extends ConfigPropertyCommand {

	@Override
	protected String getCommand() {
		return "default";
	}
	
	@Override
	protected String getHelp() {
		return "Manage default values";
	}
	
	@Override
	protected String getSection() {
		return "default";
	}
	
}
