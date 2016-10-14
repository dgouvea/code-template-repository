package coderepository;

public class MavenDependencyManagerTest extends DependencyManagerTest {

	@Override
	protected DependencyManager createDependencyManager() {
		return new MavenDependencyManager();
	}
	
	protected String pattern(String groupId, String artifactId, String version, String scope) {
		StringBuilder text = new StringBuilder();
		text.append("\n\t\t<dependency>\n\t\t\t");
		text.append("<groupId>").append(groupId).append("</groupId>");
		text.append("\n\t\t\t");
		text.append("<artifactId>").append(artifactId).append("</artifactId>");
		text.append("\n\t\t\t");
		text.append("<version>").append(version).append("</version>");
		if (scope != null) {
			text.append("\n\t\t\t");
			text.append("<scope>").append(scope).append("</scope>");
		}
		text.append("\n\t\t</dependency>");
		return text.toString();
	}
	
}
