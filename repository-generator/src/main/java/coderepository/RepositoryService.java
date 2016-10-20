package coderepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RepositoryService {

	@Autowired
	private TemplateService templateService;
	
	@Autowired
	private Config config;
	
	public Template push(String repositoryName, String templateName, byte[] bytes) {
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
