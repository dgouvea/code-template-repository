package coderepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RepositoryServiceFactory {

	@Autowired
	private ApplicationContext context;
	
	public RepositoryService local() {
		return (RepositoryService) context.getBean("localRepositoryService");
	}
	
	public RepositoryService remote(String name) {
		return (RepositoryService) context.getBean("remoteRepositoryService", name);
	}
	
}
