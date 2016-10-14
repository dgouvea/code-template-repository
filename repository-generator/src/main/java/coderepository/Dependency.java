package coderepository;

import java.util.Objects;

public final class Dependency {

	private final String group;
	private final String artifact;
	private final String version;
	private final String scope;
	private final String toString;
	
	Dependency(String group, String artifact, String version) {
		this(group, artifact, version, null);
	}
	
	Dependency(String group, String artifact, String version, String scope) {
		this.group = group;
		this.artifact = artifact;
		this.version = version;
		this.scope = scope;
		
		StringBuilder string = new StringBuilder();
		string.append(group);
		string.append(":").append(artifact);
		string.append(":").append(version);
		if (Objects.nonNull(scope) && !scope.isEmpty()) {
			string.append(":").append(scope);
		}
		this.toString = string.toString();
	}

	public String getGroup() {
		return group;
	}

	public String getArtifact() {
		return artifact;
	}

	public String getVersion() {
		return version;
	}

	public String getScope() {
		return scope;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artifact == null) ? 0 : artifact.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dependency other = (Dependency) obj;
		if (artifact == null) {
			if (other.artifact != null)
				return false;
		} else if (!artifact.equals(other.artifact))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return toString;
	}
	
}
