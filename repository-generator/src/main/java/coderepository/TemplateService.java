package coderepository;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TemplateService {

	@Autowired
	private Config config;
	
	public Templates local() {
		Templates templates = new Templates();
		
		File repositoryFolder = config.getRepositoryFolder();
		File[] files = repositoryFolder.listFiles();
		for (File file : files) {
			Template template = new Template();
			template.setName(file.getName().replace(".zip", ""));
			template.setFileName(file.getName());
			template.setSize(file.length());
			template.setDate(new Date(file.lastModified()));
			templates.add(template);
		}
		
		return templates;
	}

	public Templates remote(String name) {
		String url = config.getProperty("repository." + name);
		
		RestTemplate rest = new RestTemplate();
		return rest.getForObject(url + "/repository/templates", Templates.class);
	}
	
}
