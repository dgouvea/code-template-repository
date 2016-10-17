package coderepository.command;

import org.springframework.stereotype.Component;

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
	protected void run(String action, Args args) {
		if (args.hasNext()) {
			
		}
	}

}
