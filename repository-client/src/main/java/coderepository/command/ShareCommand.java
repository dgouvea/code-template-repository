package coderepository.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import coderepository.Template;

@Component
public class ShareCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "share";
	}

	@Override
	protected String getHelp() {
		return "share a template";
	}

	@Override
	protected void run(String action, Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should specify the template");
		}
		
		String templateName = args.next();
		
		File repositoryFolder = config.getRepositoryFolder();
		
		File templateFile = new File(repositoryFolder, templateName + ".zip");
		if (!templateFile.exists()) {
			throw new IllegalArgumentException("Template " + templateName + " does not exists");
		}
		
		if (!args.hasNext()) {
			try {
				FileUtils.copyFileToDirectory(templateFile, new File("."));
				log("Creating template package for " + templateFile);
			} catch (IOException e) {
				throw new RuntimeException("Error copying template: " + e.getMessage(), e);
			}
		} else {
			String repository = args.next();
			String url = config.getProperty("repository." + repository);
			
			share(templateName, templateFile, url);
		}
	}

	private void share(String templateName, File templateFile, String url) {
		byte[] byteArray;

		try (FileInputStream inputStream = new FileInputStream(templateFile)) {
			byteArray = IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Error reading the template file", e);
		}
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		multipartRequest.add("file", new ByteArrayResource(byteArray) {
			@Override
			public String getFilename() {
				return templateFile.getName();
			}
		});
		multipartRequest.add("filename", templateFile.getName());
		 
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, header);
		
		RestTemplate rest = new RestTemplate();
		rest.exchange(url + "/repository/push", HttpMethod.POST, requestEntity, Template.class);
	}

}
