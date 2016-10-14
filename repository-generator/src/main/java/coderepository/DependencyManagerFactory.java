package coderepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public class DependencyManagerFactory {

	private Map<String, DependencyManager> dependencyManagers = new HashMap<>();

	public DependencyManagerFactory() {
		
	}
	
	public DependencyManager getDependencyManager(String name) {
		if (dependencyManagers.isEmpty()) {
			load();
		}
		return dependencyManagers.get(name.toLowerCase());
	}
	
	public Collection<DependencyManager> getDependencyManagers() {
		if (dependencyManagers.isEmpty()) {
			load();
		}
		return dependencyManagers.values();
	}

	private void load() {
		Reflections r = new Reflections();
		Set<Class<? extends DependencyManager>> subTypes = r.getSubTypesOf(DependencyManager.class);
		for (Class<? extends DependencyManager> subType : subTypes) {
			try {
				DependencyManager dependencyManager = subType.newInstance();
				dependencyManagers.put(dependencyManager.getName().toLowerCase(), dependencyManager);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
	
}
