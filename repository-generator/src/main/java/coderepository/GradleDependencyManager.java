package coderepository;

import java.util.Objects;

public class GradleDependencyManager extends DependencyManager {

	@Override
	public String getName() {
		return "Gradle";
	}
	
	@Override
	public String getSettingFileName() {
		return "build.gradle";
	}
	
	@Override
	protected String toString(Dependency dependency) {
		StringBuilder text = new StringBuilder();
		text.append("\n\t");
		if (Objects.nonNull(dependency.getScope()) && !dependency.getScope().isEmpty()) {
			text.append(dependency.getScope()).append(" ");
		}
		text.append("group: '").append(dependency.getGroup()).append("', ");
		text.append("name: '").append(dependency.getArtifact()).append("'");
		text.append("version: '").append(dependency.getVersion()).append("'");
		return text.toString();
	}
	
	@Override
	protected String getAnchor() {
		return "// dependencies";
	}
	
}
