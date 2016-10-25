package coderepository.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.RepositoryService;
import coderepository.RepositoryServiceFactory;
import coderepository.TemplateService;
import coderepository.command.Options.Args;

@Component
public class ShareCommand extends AbstractCommandLine {

	@Autowired
	private TemplateService templateService;

	@Autowired
	private RepositoryServiceFactory repositoryServiceFactory;
	
	@Override
	protected String getCommand() {
		return "share";
	}

	@Override
	protected String getHelp() {
		return "share a template";
	}

	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		options.param("template", "The name of template to be shared");
		options.param("repository", "The name of repository where the template will be pushed");
		return options;
	}
	
	@Override
	protected void run(Args args) {
		Optional<String> templateName = args.param("template");

		if (!templateName.isPresent()) {
			throw new IllegalArgumentException("You should specify the template");
		}
		
		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		
		String templateFileName = templateService.getTemplateFileName(templateName.get());
		if (!localRepositoryService.exists(templateName.get())) {
			throw new IllegalArgumentException("Template " + templateName + " does not exists");
		}
		
		byte[] bytes = localRepositoryService.download(templateName.get());

		Optional<String> repositoryName = args.param("repository");

		if (!repositoryName.isPresent()) {
			try (FileOutputStream outputStream = new FileOutputStream(new File(templateFileName))) {
				IOUtils.write(bytes, outputStream);
				log("Creating template package for " + templateFileName);
			} catch (IOException e) {
				throw new RuntimeException("Error copying template: " + e.getMessage(), e);
			}
		} else {
			RepositoryService repositoryService = repositoryServiceFactory.remote(repositoryName.get());
			repositoryService.push(templateName.get(), bytes);
			log("Template " + templateName + " has pushed to " + repositoryName.get() + " repository");
		}
	}

}
