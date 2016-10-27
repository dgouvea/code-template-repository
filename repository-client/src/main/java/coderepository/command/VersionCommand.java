package coderepository.command;

import org.springframework.stereotype.Component;

import coderepository.command.Options.Args;

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
	public Options options() {
		Options options = new Options();
		options.option("Show the version of Code Template Repository client", "version", "v");
		return options;
	}
	
	@Override
	protected boolean canExecute(Args args) {
		return args.params().isEmpty() && args.hasOption("version");
	}
	
	@Override
	protected void run(Args args) {
		String version = config.getProperty("client.version");
		log("Code Template Repository version " + version);
		log("Developed by David Sobreira Gouvea in Poland");
	}

}
