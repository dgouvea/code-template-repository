package coderepository.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import coderepository.Config;
import coderepository.Template;
import coderepository.Templates;

@RestController
@RequestMapping("/repository")
public class RepositoryService {

	@Autowired
	private Config config;
	
	@RequestMapping(method = RequestMethod.GET, value = "/templates")
	public ResponseEntity<Templates> templates() {
		File repositoryFolder = config.getRepositoryFolder();

		List<Template> templates = Arrays.asList(repositoryFolder.listFiles()).stream().map(file -> {
			Template template = new Template();
			template.setName(file.getName().replace(".zip", ""));
			template.setFileName(file.getName());
			template.setDate(new Date());
			template.setSize(file.length());
			return template;
		}).collect(Collectors.toList());
		
		return ResponseEntity.ok(new Templates(templates));
	}
	
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/templates/{name}")
	public ResponseEntity<Resource> download(@PathVariable("name") String name) {
		File repositoryFolder = config.getRepositoryFolder();
		
		File templateFile = new File(repositoryFolder, name + ".zip");
		if (templateFile.exists()) {
			byte[] byteArray;

			try (FileInputStream inputStream = new FileInputStream(templateFile)) {
				byteArray = IOUtils.toByteArray(inputStream);
			} catch (IOException e) {
				throw new RuntimeException("Error reading the template file", e);
			}
			
			return ResponseEntity
	                .ok()
	                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+templateFile.getName()+"\"")
	                .body(new ByteArrayResource(byteArray) {
	        			@Override
	        			public String getFilename() {
	        				return templateFile.getName();
	        			}
	        		});
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ByteArrayResource(new byte[]{}));
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/push", headers = "content-type=multipart/form-data")
	public ResponseEntity<Template> push(@RequestParam("file") MultipartFile file) {
		File repositoryFolder = config.getRepositoryFolder();
		
		if (file.getContentType().equals("application/octet-stream")) {
			File templateFile = new File(repositoryFolder, file.getOriginalFilename());
			if (!templateFile.exists()) {
				Template template = new Template();
				template.setName(templateFile.getName().replace(".zip", ""));
				template.setFileName(templateFile.getName());
				template.setDate(new Date());
				template.setSize(file.getSize());
				
				try (FileOutputStream outputStream = new FileOutputStream(templateFile)) {
					IOUtils.write(file.getBytes(), outputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return ResponseEntity.ok(template);
			} else {
				return ResponseEntity.status(HttpStatus.FOUND).body(new Template());
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Template());
		}
	}

}
