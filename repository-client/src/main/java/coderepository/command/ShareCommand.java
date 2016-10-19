package coderepository.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class ShareCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "share";
	}

	@Override
	protected String getHelp() {
		return "share a template";
	}

	@Override
	protected void run(String action, Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should specify the template");
		}
		
		String templateName = args.next();
		
		File repositoryFolder = config.getRepositoryFolder();
		
		File templateFile = new File(repositoryFolder, templateName + ".zip");
		if (!templateFile.exists()) {
			throw new IllegalArgumentException("Template " + templateName + " does not exists");
		}
		
		if (!args.hasNext()) {
			copyTemplate(templateFile);
		} else {
			String repository = args.next();
			String url = config.getProperty("repository." + repository);
			
			share(templateName, templateFile, url);
		}
	}

	private void share(String templateName, File templateFile, String url) {

	}

	private void copyTemplate(File templateFile) {
		try {
			FileUtils.copyFileToDirectory(templateFile, new File("."));
			log("Creating template package for " + templateFile);
		} catch (IOException e) {
			throw new RuntimeException("Error copying template: " + e.getMessage(), e);
		}
	}

}
