package coderepository.command;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import coderepository.TemplateManager;
import coderepository.util.CompressorUtils;

@Component
public class InstallCommand extends AbstractCommandLine {

	@Override
	protected void run(Args args) {
		if (!args.hasNext()) {
			return;
		}

		String action = args.next(); 
		
		if (!action.equals("install")) {
			return;
		}
		
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
		templateManager.install(folder.getAbsolutePath());
		
		try {
			FileUtils.deleteDirectory(tmp);
		} catch (IOException e) {
		}
		
		log("Template " + templateName + " installed sucessfully");
	}

}
