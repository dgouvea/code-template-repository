package coderepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.FileUtils;

import coderepository.util.FileReplacer;

public class TemplateManager {

	private final File templateFolder;
	private final Map<String, String> properties = new HashMap<>();
	private final List<Dependency> dependencies = new ArrayList<>();
	private final DependencyManagerFactory factory = new DependencyManagerFactory();
	private String dependencyManagementSystem;
	
	public TemplateManager(String templateFolder) {
		this.templateFolder = new File(templateFolder);
	}
	
	public String getDependencyManagerSystem() {
		return dependencyManagementSystem;
	}
	
	public void setDependencyManagerSystem(String dependencyManagementSystem) {
		this.dependencyManagementSystem = dependencyManagementSystem;
	}
	
	public TemplateManager setArtifact(String name) {
		setProperty("artifact", name);
		setProperty("projectName", name);
		return this;
	}

	public TemplateManager setVersion(String version) {
		setProperty("version", version);
		return this;
	}

	public TemplateManager setPackaging(String packaging) {
		setProperty("packaging", packaging);
		return this;
	}

	public TemplateManager setGroup(String group) {
		setProperty("packageName", group);
		setProperty("packageFolder", group.replace(".", File.separator));
		return this;
	}
	
	public TemplateManager setProperty(String name, String value) {
		properties.put(name, value);
		return this;
	}

	public void dependency(String group, String artifact, String version, String scope) {
		dependencies.add(new Dependency(group, artifact, version, scope));
	}

	public void dependency(String group, String artifact, String version) {
		dependencies.add(new Dependency(group, artifact, version));
	}

	public void dependency(Dependency dependency) {
		dependencies.add(dependency);
	}
	
	public void installDependencies(String path) {
		if (dependencyManagementSystem == null || dependencies.isEmpty()) {
			return;
		}
		
		DependencyManager dependencyManager = factory.getDependencyManager(dependencyManagementSystem);
		if (dependencyManager == null) {
			return;
		}
		
		String fileName = dependencyManager.getSettingFileName();
		
		try {
			Optional<Path> settingFile = Files.find(Paths.get(path), 3, (file, attr) -> {
				return file.getFileName().endsWith(fileName);
			}).findFirst();
			if (settingFile.isPresent()) {
				dependencyManager.install(settingFile.get().toFile(), dependencies);
			}
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public void install(String path) {
		install(path, false);
	}
	
	public void install(String path, boolean override) {
		try {
			FileUtils.copyDirectory(templateFolder, new File(path));
			FileReplacer.replaceFolders(new File(path), override, properties);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		installDependencies(path);
	}

}
