package coderepository;

public class GradleDependencyManagerTest extends DependencyManagerTest {
	
	@Override
	protected DependencyManager createDependencyManager() {
		return new GradleDependencyManager();
	}

	protected String pattern(String groupId, String artifactId, String version, String scope) {
		StringBuilder text = new StringBuilder();
		text.append("\n\t");
		if (scope != null) {
			text.append(scope).append(" ");
		}
		text.append("group: '").append(groupId).append("', ");
		text.append("name: '").append(artifactId).append("'");
		text.append("version: '").append(version).append("'");
		return text.toString();
	}
	
}
