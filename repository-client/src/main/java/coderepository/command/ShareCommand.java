package coderepository.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.RepositoryService;
import coderepository.TemplateService;

@Component
public class ShareCommand extends AbstractCommandLine {

	@Autowired
	private TemplateService templateService;

	@Autowired
	private RepositoryService repositoryService;
	
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
		
		File templateFile = new File(repositoryFolder, templateService.getTemplateFileName(templateName));
		if (!templateFile.exists()) {
			throw new IllegalArgumentException("Template " + templateName + " does not exists");
		}
		
		if (!args.hasNext()) {
			try {
				FileUtils.copyFileToDirectory(templateFile, new File("."));
				log("Creating template package for " + templateFile);
			} catch (IOException e) {
				throw new RuntimeException("Error copying template: " + e.getMessage(), e);
			}
		} else {
			String repositoryName = args.next();
			
			byte[] bytes = templateService.download(templateName);
			repositoryService.push(repositoryName, templateName, bytes);
		}
	}

}
