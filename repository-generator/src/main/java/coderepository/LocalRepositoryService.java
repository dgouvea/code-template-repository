package coderepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Scope("prototype")
@Service("localRepositoryService")
public class LocalRepositoryService implements RepositoryService {

	@Autowired
	private Config config;
	
	@Autowired
	private TemplateService templateService;
	
	@Override
	public Templates list() {
		Templates templates = new Templates();
		
		File repositoryFolder = config.getRepositoryFolder();
		File[] files = repositoryFolder.listFiles(file -> file.isFile() && file.getName().endsWith(TemplateService.PACKAGE_EXTENSION));
		for (File file : files) {
			Template template = new Template();
			template.setName(templateService.getTemplateName(file.getName()));
			template.setFileName(file.getName());
			template.setSize(file.length());
			template.setDate(new Date(file.lastModified()));
			templates.add(template);
		}
		
		return templates;
	}
	
	@Override
	public boolean exists(String templateName) {
		File repositoryFolder = config.getRepositoryFolder();
		File templateFile = new File(repositoryFolder, templateService.getTemplateFileName(templateName));
		return templateFile.exists();
	}
	
	@Override
	public byte[] download(String templateName) {
		byte[] byteArray;

		File repositoryFolder = config.getRepositoryFolder();
		try (FileInputStream inputStream = new FileInputStream(new File(repositoryFolder, templateService.getTemplateFileName(templateName)))) {
			byteArray = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error reading the template file", e);
		}
		
		return byteArray;
	}
	
	@Override
	public Template push(String templateName, byte[] bytes) {
		String templateFileName = templateService.getTemplateFileName(templateName);
		try (FileOutputStream outputStream = new FileOutputStream(new File(config.getRepositoryFolder(), templateFileName))) {
			IOUtils.write(bytes, outputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error storing template " + templateName + " in local repository", e);
		}
		
		Template template = new Template();
		template.setName(templateName);
		template.setFileName(templateFileName);
		template.setDate(new Date());
		template.setSize(bytes.length);
		return template;
	}

}
