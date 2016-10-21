package coderepository.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
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
		
		String templateFileName = templateService.getTemplateFileName(templateName);
		if (!templateService.exists(templateName)) {
			throw new IllegalArgumentException("Template " + templateName + " does not exists");
		}
		
		byte[] bytes = templateService.download(templateName);

		if (!args.hasNext()) {
			try (FileOutputStream outputStream = new FileOutputStream(new File(templateFileName))) {
				IOUtils.write(bytes, outputStream);
				log("Creating template package for " + templateFileName);
			} catch (IOException e) {
				throw new RuntimeException("Error copying template: " + e.getMessage(), e);
			}
		} else {
			String repositoryName = args.next();
			repositoryService.push(repositoryName, templateName, bytes);
			log("Template " + templateName + " has pushed to " + repositoryName + " repository");
		}
	}

}
