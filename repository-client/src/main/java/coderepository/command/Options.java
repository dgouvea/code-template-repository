package coderepository.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import coderepository.Dependency;

public class Options {

	private final List<Parameter> params = new ArrayList<>();
	private final List<Option> options = new ArrayList<>();
	
	public void param(String name, String help) {
		this.params.add(new Parameter(name, help));
	}
	
	public void option(String help, String... options) {
		option(help, false, options);
	}
	
	public void option(String help, boolean value, String... options) {
		this.options.add(new Option(options[0], help, value, Arrays.asList(options)));
	}

	public Parameter getParameter(int index) {
		return params.get(index);
	}

	public Option getOption(String name) {
		return options.stream().filter(o -> o.getName().equals(name)).findFirst().get();
	}
	
	public Args parse(String[] data) {
		int index = 0;
		
		Map<String, String> args = new HashMap<>();
		Map<String, String> opts = new HashMap<>();
		List<Dependency> dependencies = new ArrayList<>();
		
		List<String> list = Arrays.asList(data);
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String arg = it.next();
			
			if (arg.startsWith("-")) {
				final String optionName = arg.substring(1);
				String optionKey = optionName;
				boolean hasValue = true;
				
				Optional<Option> option = options.stream().filter(o -> o.options.contains(optionName)).findFirst();
				if (option.isPresent()) {
					Option o = option.get();
					optionKey = o.name;
					hasValue = o.value;
				}

				String value = "";
				if (hasValue && it.hasNext()) {
					value = it.next();
				}
				opts.put(optionKey, value);
			} else if (arg.startsWith("+")) {
				String[] parts = arg.substring(1).split("\\:");
				if (parts.length == 3) {
					dependencies.add(new Dependency(parts[0], parts[1], parts[2]));
				} else if (parts.length == 4) {
					dependencies.add(new Dependency(parts[0], parts[1], parts[2], parts[3]));
				}
			} else {
				if (params.size() > index) {
					Parameter option = params.get(index++);
					args.put(option.name, arg);
				}
			}
 		}
		
		return new Args(args, opts, dependencies);
	}
	
	public class Parameter {
		
		private final String name;
		private final String help;
		
		private Parameter(String name, String help) {
			this.name = name;
			this.help = help;
		}

		public String getName() {
			return name;
		}

		public String getHelp() {
			return help;
		}
		
	}

	public class Option {
		
		private final String name;
		private final List<String> options;
		private final String help;
		private final boolean value;
		
		private Option(String name, String help, boolean value, List<String> options) {
			this.name = name;
			this.help = help;
			this.value = value;
			this.options = options;
		}

		public String getName() {
			return name;
		}

		public List<String> getOptions() {
			return options;
		}

		public String getHelp() {
			return help;
		}

		public boolean hasValue() {
			return value;
		}
		
	}
	
	public static class Args {
		
		private final Map<String, String> args;
		private final Map<String, String> options;
		private final List<Dependency> dependencies;

		private Args(Map<String, String> args, Map<String, String> options, List<Dependency> dependencies) {
			this.args = args;
			this.options = options;
			this.dependencies = dependencies;
		}

		public Optional<String> param(String name) {
			return Optional.ofNullable(args.get(name));
		}

		public Map<String, String> params() {
			return Collections.unmodifiableMap(args);
		}

		public boolean hasOption(String... names) {
			for (String name : names) {
				if (options.containsKey(name)) {
					return true;
				}
			}
			return false;
		}

		public Optional<String> option(String name) {
			return Optional.ofNullable(options.get(name));
		}

		public Map<String, String> options() {
			return Collections.unmodifiableMap(options);
		}

		public List<Dependency> dependencies() {
			return dependencies;
		}
		
	}

}
