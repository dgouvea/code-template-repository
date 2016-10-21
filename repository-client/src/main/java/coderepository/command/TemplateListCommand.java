package coderepository.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

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
			
			try {
				templateService.remote(repositoryName).forEach(template -> {
					log("\t* " + template.getName());
				});
			} catch (ResourceAccessException e) {
				log("\t> Cannot access this repository: " + e.getMessage());
			}
		});
	}

}
