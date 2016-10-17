package coderepository.command;

import org.springframework.stereotype.Component;

@Component
public class VersionCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "";
	}
	
	@Override
	protected String getHelp() {
		return "Display the version";
	}
	
	@Override
	protected boolean canExecute(String action, Args args) {
		return action.isEmpty() && args.getOptions().has("v", "version");
	}
	
	@Override
	protected void run(String action, Args args) {
		String version = config.getProperty("client.version");
		log("Code Template Repository version " + version);
		log("Developed by David Sobreira Gouvea in Poland");
	}

}
