package coderepository.command;

import org.springframework.stereotype.Component;

@Component
public class VersionCommand extends AbstractCommandLine {

	@Override
	protected void run(Args args) {
		if (!args.hasNext()) {
			return;
		}
		
		String param = args.next();
		
		if (param.equals("-v") || param.equals("-version")) {
			String version = config.getProperty("client.version");
			log("Code Template Repository version " + version);
			log("Developed by David Sobreira Gouvea in Poland");
		}
	}

}
