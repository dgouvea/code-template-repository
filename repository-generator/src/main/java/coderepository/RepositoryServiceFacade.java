package coderepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryServiceFacade {

	@Autowired
	private Config config;

	@Autowired
	private RepositoryServiceFactory repositoryServiceFactory;
	
	public boolean exists(String templateName) {
		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		if (localRepositoryService.exists(templateName)) {
			return true;
		}
		
		Set<String> reposiories = config.getSection("repository").keySet();
		for (String key : reposiories) {
			String repositoryName = key.replace("repository.", "");
			
			RepositoryService repositoryService = repositoryServiceFactory.remote(repositoryName);
			if (repositoryService.exists(templateName)) {
				return true;
			}
		}

		return false;
	}

	public byte[] download(String templateName) {
		RepositoryService localRepositoryService = repositoryServiceFactory.local();
		if (localRepositoryService.exists(templateName)) {
			return localRepositoryService.download(templateName);
		}
		
		Set<String> reposiories = config.getSection("repository").keySet();
		for (String key : reposiories) {
			String repositoryName = key.replace("repository.", "");
			
			RepositoryService repositoryService = repositoryServiceFactory.remote(repositoryName);
			if (repositoryService.exists(templateName)) {
				return repositoryService.download(templateName);
			}
		}
		
		throw new IllegalArgumentException("Template " + templateName + " not found");
	}
	
}
