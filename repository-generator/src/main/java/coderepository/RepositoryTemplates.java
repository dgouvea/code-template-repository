package coderepository;

public class RepositoryTemplates {

	private final String repositoryName;
	private final Templates templates;

	public RepositoryTemplates(String repositoryName, Templates templates) {
		this.repositoryName = repositoryName;
		this.templates = templates;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public Templates getTemplates() {
		return templates;
	}

	@Override
	public String toString() {
		return "RepositoryTemplates [repositoryName=" + repositoryName + ", templates=" + templates + "]";
	}
	
}
