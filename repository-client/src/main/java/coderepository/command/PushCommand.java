package coderepository.command;

import java.io.File;

import org.springframework.stereotype.Component;

import coderepository.util.CompressorUtils;

@Component
public class PushCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "push";
	}
	
	@Override
	protected String getHelp() {
		return "Push a template to the local repository";
	}
	
	@Override
	protected void run(String action, Args args) {
		String folderName = ".";
		if (args.hasNext()) {
			folderName = args.next();
		}
		
		File folder = new File(folderName);

		String templateName = folder.getName();
		if (args.hasNext()) {
			templateName = args.next();
		}
		
		CompressorUtils.compress(new File(config.getRepositoryFolder(), templateName + ".zip"), folder);
		
		log("Template \"" + templateName + "\" created in the local repository");
	}

}
