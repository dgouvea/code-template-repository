package coderepository.command;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import coderepository.TemplateManager;
import coderepository.util.CompressorUtils;

@Component
public class InstallCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "install";
	}
	
	@Override
	protected String getHelp() {
		return "Install a template";
	}
	
	@Override
	protected void run(String action, Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the template name");
		}
		
		String templateName = args.next();
		
		String folderName = ".";
		if (args.hasNext()) {
			folderName = args.next();
		}
		
		File folder = new File(folderName);
		File templateFile = new File(config.getRepositoryFolder(), templateName + ".zip");
		if (!templateFile.exists()) {
			throw new RuntimeException("Template " + templateName + " not found");
		}
		
		File tmp = new File(System.getProperty("java.io.tmpdir"), templateName);
		tmp.mkdirs();

		CompressorUtils.uncompress(templateFile, tmp);
		
		TemplateManager templateManager = new TemplateManager(tmp.getAbsolutePath());
		templateManager.setGroup(config.getProperty("default.group"));
		templateManager.setArtifact(config.getProperty("default.artifact"));
		templateManager.setVersion(config.getProperty("default.version"));
		templateManager.setPackaging(config.getProperty("default.packaging"));

		Options options = args.getOptions();
		if (options.has("group", "g")) {
			templateManager.setGroup(options.get("group", "g").getValue());
		}
		if (options.has("artifact", "a")) {
			templateManager.setArtifact(options.get("artifact", "a").getValue());
		}
		if (options.has("version", "v")) {
			templateManager.setVersion(options.get("version", "v").getValue());
		}
		if (options.has("packaging")) {
			templateManager.setPackaging(options.get("packaging").getValue());
		}
		
		options.forEach(option -> {
			templateManager.setProperty(option.getOption(), option.getValue());
		});
		
		List<String> dependencies = args.getDependencies();
		dependencies.forEach(dependency -> {
			String[] parts = dependency.split(":");
			if (parts.length == 3) {
				templateManager.dependency(parts[0], parts[1], parts[2]);
			} else if (parts.length == 4) {
				templateManager.dependency(parts[0], parts[1], parts[2], parts[3]);
			}
		});
		
		templateManager.install(folder.getAbsolutePath());
		
		try {
			FileUtils.deleteDirectory(tmp);
		} catch (IOException e) {
		}
		
		log("Template " + templateName + " installed sucessfully");
	}

}
