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
		
		if (!repositoryServiceFacade.exists(templateName)) {
			throw new RuntimeException("Template " + templateName + " not found");
		}
		
		File tmp = new File(System.getProperty("java.io.tmpdir"), templateName);
		tmp.mkdirs();

		File packageFile = new File(tmp, templateService.getTemplateFileName(templateName));
		
		byte[] bytes = repositoryServiceFacade.download(templateName);
		try (FileOutputStream outputStream = new FileOutputStream(packageFile)) {
			IOUtils.write(bytes, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error downloading the template", e);
		}
		
		CompressorUtils.uncompress(packageFile, tmp);
		
		TemplateManager templateManager = new TemplateManager(tmp.getAbsolutePath());
		templateManager.setDependencyManagerSystem(config.getProperty("default.builder"));
		templateManager.setGroup(config.getProperty("default.group"));
		templateManager.setArtifact(config.getProperty("default.artifact"));
		templateManager.setVersion(config.getProperty("default.version"));
		templateManager.setPackaging(config.getProperty("default.packaging"));

		args.options().forEach((name, value) -> {
			templateManager.setProperty(name, value);
		});
		
		args.dependencies().forEach(dependency -> templateManager.dependency(dependency));
		
		templateManager.install(folder.getAbsolutePath());
		
		try {
			FileUtils.deleteDirectory(tmp);
		} catch (IOException e) {
		}
		
		log("Template " + templateName + " installed sucessfully");
	}

}
