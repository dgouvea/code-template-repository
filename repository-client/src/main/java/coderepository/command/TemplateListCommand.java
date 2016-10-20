package coderepository.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import coderepository.TemplateService;

@Component
public class TemplateListCommand extends AbstractCommandLine {

	@Autowired
	private TemplateService templateService;
	
	@Override
	protected String getCommand() {
		return "list";
	}

	@Override
	protected String getHelp() {
		return "List all templates from local repository";
	}

	@Override
	protected void run(String action, Args args) {
		log("## local");
		templateService.local().forEach(template -> {
			log("\t* " + template.getName());
		});
		
		config.getSection("repository").forEach((key, value) -> {
			String repositoryName = key.replace("repository.", "");
			log("\n## remote: " + repositoryName + " (" + value + ")");
			
			templateService.remote(repositoryName).forEach(template -> {
				log("\t* " + template.getName());
			});
		});
	}

}
