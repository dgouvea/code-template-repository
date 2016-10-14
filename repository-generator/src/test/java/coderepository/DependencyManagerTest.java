package coderepository;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public abstract class DependencyManagerTest {

	@Test
	public void testOneDependency() {
		List<Dependency> dependencies = new ArrayList<>();
		dependencies.add(new Dependency("junit", "junit", "4.12"));
		
		DependencyManager dependencyManager = createDependencyManager();
		String string = dependencyManager.toString(dependencies);
		
		Assert.assertEquals(pattern("junit", "junit", "4.12"), string);
	}
	
	@Test
	public void testTwoDependency() {
		List<Dependency> dependencies = new ArrayList<>();
		dependencies.add(new Dependency("junit", "junit", "4.12", "test"));
		dependencies.add(new Dependency("commons-io", "commons-io", "2.5"));
		
		DependencyManager dependencyManager = createDependencyManager();
		String string = dependencyManager.toString(dependencies);
		
		StringBuilder expected = new StringBuilder();
		expected.append(pattern("junit", "junit", "4.12", "test"));
		expected.append(pattern("commons-io", "commons-io", "2.5"));
		
		Assert.assertEquals(expected.toString(), string);
	}
	
	protected String pattern(String groupId, String artifactId, String version) {
		return pattern(groupId, artifactId, version, null);
	}

	protected abstract DependencyManager createDependencyManager();

	protected abstract String pattern(String groupId, String artifactId, String version, String scope);
	
}
