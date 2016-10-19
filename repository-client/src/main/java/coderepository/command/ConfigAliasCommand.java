package coderepository.command;

import org.springframework.stereotype.Component;

@Component
public class ConfigAliasCommand extends ConfigPropertyCommand {

	@Override
	protected String getCommand() {
		return "alias";
	}
	
	@Override
	protected String getHelp() {
		return "Manage the dependency aliases";
	}
	
	@Override
	protected String getSection() {
		return "dependency";
	}
	
	@Override
	protected String getPrefix() {
		return "alias.";
	}
	
	@Override
	protected void validate(String name, String value) {
		String[] parts = value.split("\\:");
		if (parts.length != 3 && parts.length != 4) {
			throw new IllegalArgumentException("Invalid dependency format, the pattern is group:artifact:version or group:artifact:version:scope");
		}
	}

}
