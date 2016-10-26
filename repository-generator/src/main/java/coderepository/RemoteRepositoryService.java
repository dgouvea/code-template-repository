package coderepository;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Scope("prototype")
@Service("remoteRepositoryService")
public class RemoteRepositoryService implements RepositoryService {

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private Config config;

	private String repositoryName;
	
	public RemoteRepositoryService(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	
	@Override
	public Templates list() {
		String url = config.getProperty("repository." + repositoryName);
		
		RestTemplate rest = new RestTemplate();
		return rest.getForObject(url + "/repository/templates", Templates.class);
	}

	@Override
	public boolean exists(String templateName) {
		Templates templates = list();
		for (Template template : templates) {
			if (template.getName().equals(templateName)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public byte[] download(String templateName) {
		String url = config.getProperty("repository." + repositoryName);
		
		RestTemplate rest = new RestTemplate();
		rest.getMessageConverters().add(
	            new ByteArrayHttpMessageConverter());

	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
	    
	    HttpEntity<String> entity = new HttpEntity<String>(headers);
	    
		ResponseEntity<byte[]> response = rest.exchange(url + "/repository/templates/" + templateName, HttpMethod.GET, entity, byte[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	    	throw new RuntimeException("Problem to get template");
	    }
	}
	
	@Override
	public Template push(String templateName, byte[] bytes) {
		String templateFileName = templateService.getTemplateFileName(templateName);
		
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		MultiValueMap<String, Object> multipartRequest = new LinkedMultiValueMap<>();
		multipartRequest.add("file", new ByteArrayResource(bytes) {
			@Override
			public String getFilename() {
				return templateFileName;
			}
		});
		multipartRequest.add("filename", templateFileName);
		 
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(multipartRequest, header);
		
		String url = config.getProperty("repository." + repositoryName);
		
		RestTemplate rest = new RestTemplate();
		ResponseEntity<Template> entity = rest.postForEntity(url + "/repository/push", requestEntity, Template.class);
		return entity.getBody();
	}
	
}
