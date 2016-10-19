package coderepository.command;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Component;

@Component
public class ConfigRepositoryCommand extends ConfigPropertyCommand {

	@Override
	protected String getCommand() {
		return "repository";
	}

	@Override
	protected String getHelp() {
		return "Manage remote repositories";
	}
	
	@Override
	protected String getSection() {
		return "repository";
	}
	
	@Override
	protected void validate(String name, String value) {
		try {
			new URL(value);
		} catch (MalformedURLException e) {
			throw new IllegalArgumentException("Invalid repository URL", e);
		}
	}

}
