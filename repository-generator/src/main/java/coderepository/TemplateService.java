package coderepository;

import org.springframework.stereotype.Component;

@Component
public class TemplateService {

	static final String PACKAGE_EXTENSION = ".zip";
	
	public String getTemplateFileName(String templateName) {
		return templateName + PACKAGE_EXTENSION;
	}

	public String getTemplateName(String templateFileName) {
		return templateFileName.replace(PACKAGE_EXTENSION, "");
	}

}
