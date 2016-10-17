package coderepository.command;

import java.io.File;

import org.springframework.stereotype.Component;

@Component
public class TemplateListCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "list";
	}

	@Override
	protected String getHelp() {
		return "List all templates from local repository";
	}

	@Override
	protected void run(String action, Args args) {
		File repositoryFolder = config.getRepositoryFolder();
		File[] files = repositoryFolder.listFiles();
		for (File file : files) {
			log(" * " + file.getName().replace(".zip", ""));
		}
	}

}
