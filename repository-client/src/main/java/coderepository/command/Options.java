package coderepository.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import coderepository.command.Options.OptionValue;

public class Options implements Iterable<OptionValue> {

	private final Map<String, OptionValue> options = new HashMap<>();
	
	Options add(String option, String value) {
		options.put(option, new OptionValue(option, value));
		return this;
	}

	Options add(String option) {
		options.put(option, new OptionValue(option, null));
		return this;
	}
	
	public boolean has(String... option) {
		for (String opt : option) {
			if (options.containsKey(opt)) {
				return true;
			}
		}
		return false;
	}
	
	public OptionValue get(String... option) {
		for (String opt : option) {
			if (options.containsKey(opt)) {
				return options.get(opt);
			}
		}
		return null;
	}
	
	@Override
	public Iterator<OptionValue> iterator() {
		return options.values().iterator();
	}
	
	public static final class OptionValue {

		private final String option;
		private final String value;
		
		private OptionValue(String option, String value) {
			this.option = option;
			this.value = value;
		}

		public String getOption() {
			return option;
		}
		
		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "OptionValue [option=" + option + ", value=" + value + "]";
		}
		
	}
	
}
