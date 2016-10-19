package coderepository;

import java.util.Iterator;
import java.util.List;

public class Templates implements Iterable<Template> {

	private List<Template> templates;

	public Templates() {

	}

	public Templates(List<Template> templates) {
		this.templates = templates;
	}

	public List<Template> getTemplates() {
		return templates;
	}

	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}

	@Override
	public Iterator<Template> iterator() {
		return templates.iterator();
	}
	
	@Override
	public String toString() {
		return "Templates " + templates;
	}
	
}
