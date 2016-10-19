package coderepository.command;

import java.io.File;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import coderepository.Templates;

@Component
public class TemplateListCommand extends AbstractCommandLine {

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
		File repositoryFolder = config.getRepositoryFolder();
		File[] files = repositoryFolder.listFiles();
		for (File file : files) {
			log("\t* " + file.getName().replace(".zip", ""));
		}
		config.getSection("repository").entrySet().forEach(e -> {
			log("\n## remote: " + e.getKey().replace("repository.", "") + " (" + e.getValue() + ")");
			
			RestTemplate rest = new RestTemplate();
			Templates templates = rest.getForObject(e.getValue() + "/repository/templates", Templates.class);
			templates.forEach(template -> {
				log("\t* " + template.getName());
			});
		});
	}

}
