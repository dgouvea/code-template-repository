package coderepository.command;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.RepositoryServiceFacade;
import coderepository.TemplateManager;
import coderepository.TemplateService;
import coderepository.command.Options.Args;
import coderepository.util.CompressorUtils;

@Component
public class InstallCommand extends AbstractCommandLine {

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private RepositoryServiceFacade repositoryServiceFacade;
	
	@Override
	protected String getCommand() {
		return "install";
	}
	
	@Override
	protected String getHelp() {
		return "Install a template";
	}
	
	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		options.param("template", "The name of template to be installed");
		options.param("folder", "The destination folder where the template will be installed");
		options.option("The group name", true, "group", "g");
		options.option("The artifact name", true, "artifact", "a");
		options.option("The version of artifact", true, "version", "v");
		options.option("The packaging type", true, "packaging");
		options.option("The Builder system to be used. e.g.: Maven, Gradle", true, "builder", "b");
		return options;
	}
	
	@Override
	protected void run(Args args) {
		if (!args.param("template").isPresent()) {
			throw new IllegalArgumentException("You should provide the template name");
		}
		
		String templateName = args.param("template").get();
		
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

		logger.info("Installing template " + templateName + " on " + folder.getPath());
		
		if (!repositoryServiceFacade.exists(templateName)) {
			throw new RuntimeException("Template " + templateName + " not found");
		}
		
		File tmp = new File(System.getProperty("java.io.tmpdir"), templateName);
		tmp.mkdirs();

		File packageFile = new File(tmp, templateService.getTemplateFileName(templateName));
		
		logger.info("Downloading template " + templateName + " from repository");
		
		byte[] bytes = repositoryServiceFacade.download(templateName);
		try (FileOutputStream outputStream = new FileOutputStream(packageFile)) {
			IOUtils.write(bytes, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading the template", e);
		}
		
		logger.info("Uncompressing template file");
		
		CompressorUtils.uncompress(packageFile, tmp);
		
		packageFile.delete();
		
		logger.info("Preparing template to be installed");

		logger.info("=> Organizing template properties");
		
		TemplateManager templateManager = new TemplateManager(config, tmp.getAbsolutePath());
		templateManager.setDependencyManagerSystem(config.getProperty("default.builder"));
		templateManager.setGroup(config.getProperty("default.group"));
		templateManager.setArtifact(config.getProperty("default.artifact"));
		templateManager.setVersion(config.getProperty("default.version"));
		templateManager.setPackaging(config.getProperty("default.packaging"));

		args.options().forEach((name, value) -> {
			templateManager.setProperty(name, value);
		});

		if (args.option("group").isPresent()) {
			templateManager.setGroup(args.option("group").get());
		}
		
		if (args.option("artifact").isPresent()) {
			templateManager.setArtifact(args.option("artifact").get());
		}

		if (args.option("builder").isPresent()) {
			templateManager.setDependencyManagerSystem(args.option("builder").get());
		}
		
		logger.debug(templateManager.getProperties());
		
		logger.info("=> Organizing template dependencies");
		
		args.dependencies().forEach(dependency -> templateManager.dependency(dependency));
		
		logger.debug(args.dependencies());
		
		logger.info("Installing template...");
		
		templateManager.install(folder.getAbsolutePath());
		
		logger.info("Deleting temporary template directory");
		
		try {
			FileUtils.deleteDirectory(tmp);
		} catch (IOException e) {
		}
		
		log("Template " + templateName + " installed sucessfully");
	}

}
