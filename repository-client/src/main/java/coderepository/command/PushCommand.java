package coderepository.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.RepositoryService;
import coderepository.RepositoryServiceFactory;
import coderepository.TemplateService;
import coderepository.command.Options.Args;
import coderepository.util.CompressorUtils;

@Component
public class PushCommand extends AbstractCommandLine {

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private RepositoryServiceFactory repositoryServiceFactory;
	
	@Override
	protected String getCommand() {
		return "push";
	}
	
	@Override
	protected String getHelp() {
		return "Push a template to the local repository";
	}
	
	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		options.param("folder", "The folder to be pushed to the local repository");
		options.param("template", "The name of the template, by default it is the folder's name");
		return options;
	}
	
	@Override
	protected void run(Args args) {
		String fileName = ".";
		if (args.param("folder").isPresent()) {
			fileName = args.param("folder").get();
		}
		
		byte[] bytes;
		String templateName;

		File file = new File(fileName);
		
		if (file.isFile() && file.exists()) {
			templateName = templateService.getTemplateName(file.getName());
			
			if (!CompressorUtils.isZip(file)) {
				throw new IllegalArgumentException("The template file must be a zip file or a folder");
			}
			
			try (FileInputStream inputStream = new FileInputStream(file)) {
				bytes = IOUtils.toByteArray(inputStream);
			} catch (IOException e) {
				throw new RuntimeException("Error reading file " + file.getPath(), e);
			}
		} else {
			File folder;

			try {
				folder = file.getCanonicalFile();
			} catch (IOException e) {
				throw new RuntimeException("Problem in folder " + fileName, e);
			}

			templateName = templateService.getTemplateName(folder.getName());
	
			logger.info("Compressing folder " + folder.getPath());
			bytes = CompressorUtils.compressToBytes(folder);
		}
		
		if (args.param("template").isPresent()) {
			templateName = args.param("template").get();
		}
		
		logger.info("Pushing template " + templateName + " to the local repository");

		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		localRepositoryService.push(templateName, bytes);
		
		log("Template \"" + templateName + "\" created in the local repository");
	}

}
