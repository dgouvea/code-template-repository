package coderepository.command;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.RepositoryService;
import coderepository.RepositoryServiceFactory;
import coderepository.command.Options.Args;
import coderepository.util.CompressorUtils;

@Component
public class PushCommand extends AbstractCommandLine {

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
		String folderName = ".";
		if (args.param("folder").isPresent()) {
			folderName = args.param("folder").get();
		}
		
		File folder = new File(folderName);
		try {
			folder = folder.getCanonicalFile();
		} catch (IOException e) {
			throw new RuntimeException("Problem in folder " + folderName, e);
		}

		String templateName = folder.getName();
		if (args.param("template").isPresent()) {
			templateName = args.param("template").get();
		}

		logger.info("Compressing folder " + folder.getPath());
		
		byte[] bytes = CompressorUtils.compressToBytes(folder);

		logger.info("Pushing template " + templateName + " to the local repository");

		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		localRepositoryService.push(templateName, bytes);
		
		log("Template \"" + templateName + "\" created in the local repository");
	}

}
