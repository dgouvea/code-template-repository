package coderepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Config {

	private final Properties properties = new Properties();
	private final List<String> sections = Arrays.asList("client", "default", "repository", "dependency");

	private File settingsFolder;
	private File repositoryFolder;
	private File configFile;

	@Value("${application.version}")
	private String version;
	
	public List<String> getSections() {
		return sections;
	}
	
	public File getRepositoryFolder() {
		return repositoryFolder;
	}
	
	public Map<String, String> getProperties() {
		Map<String, String> map = new HashMap<>();
		properties.forEach((key, value) -> map.put(key.toString(), value.toString()));
		return Collections.unmodifiableMap(map);
	}
	
	public boolean containsProperty(String name) {
		return properties.containsKey(name);
	}
	
	public String getProperty(String name) {
		return properties.getProperty(name);
	}
	
	public void setProperty(String section, String name, String value) {
		properties.setProperty(section + "." + name, value);
	}
	
	public void removeProperty(String section, String name) {
		properties.remove(section + "." + name);
	}
	
	public Map<String, String> getSection(String section) {
		if (!sections.contains(section)) {
			return Collections.emptyMap();
		}
		
		return getProperties().entrySet().stream()
				.filter(e -> e.getKey().startsWith(section + "."))
				.collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
	}
	
	public void store() {
		try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
			properties.put("client.version", version);
			properties.store(outputStream, null);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	@PostConstruct
	private void setup() throws IOException {
		File home = new File(System.getProperty("user.home"));
		
		settingsFolder = new File(home, ".codetemplaterepository");
		if (!settingsFolder.exists()) {
			settingsFolder.mkdir();
		}
		
		repositoryFolder = new File(settingsFolder, "repository");
		if (!repositoryFolder.exists()) {
			repositoryFolder.mkdir();
		}

		configFile = new File(settingsFolder, "config.properties");
		if (!configFile.exists()) {
			try (FileOutputStream outputStream = new FileOutputStream(configFile)) {
				Properties properties = new Properties();
				properties.put("client.version", version);
				properties.store(outputStream, null);
			}
		}
		
		load();
	}

	private void load() throws IOException {
		File home = new File(System.getProperty("user.home"));
		
		File settings = new File(home, ".codetemplaterepository");
		File config = new File(settings, "config.properties");
		
		try (FileInputStream inputStream = new FileInputStream(config)) {
			properties.load(inputStream);
		}
	}

}
