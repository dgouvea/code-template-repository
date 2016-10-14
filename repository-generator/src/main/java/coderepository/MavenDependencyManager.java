package coderepository;

import java.util.Objects;

public class MavenDependencyManager extends DependencyManager {

	@Override
	public String getName() {
		return "Maven";
	}
	
	@Override
	public String getSettingFileName() {
		return "pom.xml";
	}
	
	@Override
	protected String toString(Dependency dependency) {
		StringBuilder xml = new StringBuilder();
		xml.append("\n\t\t<dependency>");
		if (Objects.nonNull(dependency.getGroup()) && !dependency.getGroup().isEmpty()) {
			xml.append("\n\t\t\t<groupId>").append(dependency.getGroup()).append("</groupId>");
		}
		if (Objects.nonNull(dependency.getArtifact()) && !dependency.getArtifact().isEmpty()) {
			xml.append("\n\t\t\t<artifactId>").append(dependency.getArtifact()).append("</artifactId>");
		}
		if (Objects.nonNull(dependency.getVersion()) && !dependency.getVersion().isEmpty()) {
			xml.append("\n\t\t\t<version>").append(dependency.getVersion()).append("</version>");
		}
		if (Objects.nonNull(dependency.getScope()) && !dependency.getScope().isEmpty()) {
			xml.append("\n\t\t\t<scope>").append(dependency.getScope()).append("</scope>");
		}
		xml.append("\n\t\t</dependency>");
		return xml.toString();
	}

	@Override
	protected String getAnchor() {
		return "<!-- dependencies -->";
	}
	
}
