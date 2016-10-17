package coderepository.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Args implements Iterable<String> {

	private final List<String> args = new ArrayList<>();
	private final List<String> dependencies = new ArrayList<>();
	private final Options options = new Options();
	private final Iterator<String> iterator;
	
	public Args(String... args) {
		String lastOption = null;

		for (String arg : args) {
			if (arg.startsWith("+")) {
				dependencies.add(arg.substring(1));
			} else if (arg.startsWith("-")) {
				String option = arg.substring(1);
				options.add(option);
				lastOption = option;
			} else if (lastOption != null) {
				options.add(lastOption, arg);
				lastOption = null;
			} else {
				this.args.add(arg);
			}
		}
		
		this.iterator = this.args.iterator();
	}

	public List<String> getDependencies() {
		return Collections.unmodifiableList(dependencies);
	}
	
	public Options getOptions() {
		return options;
	}
	
	public boolean hasNext() {
		return iterator.hasNext();
	}

	public String next() {
		return iterator.next();
	}
	
	@Override
	public Iterator<String> iterator() {
		return args.iterator();
	}
	
}
