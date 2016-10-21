package coderepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TemplateService {

	private static final String PACKAGE_EXTENSION = ".zip";
	
	@Autowired
	private Config config;
	
	public String getTemplateFileName(String templateName) {
		return templateName + PACKAGE_EXTENSION;
	}

	public String getTemplateName(String templateFileName) {
		return templateFileName.replace(PACKAGE_EXTENSION, "");
	}
	
	public Templates local() {
		Templates templates = new Templates();
		
		File repositoryFolder = config.getRepositoryFolder();
		File[] files = repositoryFolder.listFiles(file -> file.isFile() && file.getName().endsWith(PACKAGE_EXTENSION));
		for (File file : files) {
			Template template = new Template();
			template.setName(getTemplateName(file.getName()));
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
	
	public boolean exists(String templateName) {
		File repositoryFolder = config.getRepositoryFolder();
		File templateFile = new File(repositoryFolder, getTemplateFileName(templateName));
		return templateFile.exists();
	}
	
	public byte[] download(String templateName) {
		byte[] byteArray;

		File repositoryFolder = config.getRepositoryFolder();
		try (FileInputStream inputStream = new FileInputStream(new File(repositoryFolder, getTemplateFileName(templateName)))) {
			byteArray = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error reading the template file", e);
		}
		
		return byteArray;
	}

}
