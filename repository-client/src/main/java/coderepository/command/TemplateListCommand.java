package coderepository.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import coderepository.RepositoryService;
import coderepository.RepositoryServiceFactory;
import coderepository.command.Options.Args;

@Component
public class TemplateListCommand extends AbstractCommandLine {

	@Autowired
	private RepositoryServiceFactory repositoryServiceFactory;
	
	@Override
	protected String getCommand() {
		return "list";
	}

	@Override
	protected String getHelp() {
		return "List all templates from local repository";
	}

	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		return options;
	}
	
	@Override
	protected void run(Args args) {
		log("## local");
		
		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		localRepositoryService.list().forEach(template -> {
			log("\t* " + template.getName());
		});
		
		config.getSection("repository").forEach((key, value) -> {
			String repositoryName = key.replace("repository.", "");
			log("\n## remote: " + repositoryName + " (" + value + ")");
			
			try {
				RepositoryService repositoryService = repositoryServiceFactory.remote(repositoryName);
				repositoryService.list().forEach(template -> {
					log("\t* " + template.getName());
				});
			} catch (ResourceAccessException e) {
				log("\t> Cannot access this repository: " + e.getMessage());
			}
		});
	}

}
