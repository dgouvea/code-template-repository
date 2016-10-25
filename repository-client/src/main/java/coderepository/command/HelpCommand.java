package coderepository.command;

import org.springframework.stereotype.Component;

import coderepository.command.Options.Args;

@Component
public class HelpCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "help";
	}

	@Override
	protected String getHelp() {
		return "";
	}

	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		return options;
	}
	
	@Override
	protected void run(Args args) {

	}

}
