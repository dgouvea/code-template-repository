package coderepository.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	
	@RequestMapping(method = RequestMethod.POST, value = "/push")
	public ResponseEntity<Template> push(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		File repositoryFolder = config.getRepositoryFolder();
		
		if (!file.getContentType().contains("application/zip") 
				&& !file.getContentType().equals("application/octet-stream")) {
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
			}
		}
		
		return null;
	}

}
