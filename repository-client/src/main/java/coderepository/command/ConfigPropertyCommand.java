package coderepository.command;

import java.util.Map;

import org.springframework.stereotype.Component;

import coderepository.command.Options.Args;

@Component
public abstract class ConfigPropertyCommand extends AbstractCommandLine {

	protected abstract String getSection();

	protected String getPrefix() {
		return "";
	}
	
	protected void validate(String name, String value) {
		
	}
	
	@Override
	protected boolean canExecute(Args args) {
		return args.param(getCommand()).isPresent() && (args.param(getCommand()).get().equals(getCommand())
				|| args.param(getCommand()).get().equals("remove-" + getCommand()));
	}
	
	@Override
	public Options options() {
		Options options = new Options();
		options.param("config", "config");
		options.param(getCommand(), getHelp());
		options.param("name", "The property name");
		options.param("value", "The property value");
		return options;
	}
	
	@Override
	protected void run(Args args) {
		if (args.param(getCommand()).get().equals(getCommand())) {
			if (args.param("name").isPresent()) {
				addProperty(args);
			} else {
				showProperties();
			}
		} else if (args.param(getCommand()).get().equals("remove-" + getCommand())) {
			removeProperty(args);
		}
	}

	private void showProperties() {
		StringBuilder text = new StringBuilder();
		
		String section = getSection();
		text.append("## ").append(section).append(" ").append(getPrefix().replace(".", " ")).append("\n");
		Map<String, String> properties = config.getSection(section);
		properties.forEach((name, value) -> {
			text.append(name.replace(section + "." + getPrefix(), "\t")).append(" = ").append(value).append("\n");
		});
		
		log(text.toString());
	}

	private void addProperty(Args args) {
		if (!args.param("name").isPresent()) {
			throw new IllegalArgumentException("You should provide the name and value");
		}
		
		String name = args.param("name").get();
		
		if (!args.param("value").isPresent()) {
			throw new IllegalArgumentException("You should provide the value");
		}

		String value = args.param("value").get();
		
		validate(name, value);
		
		config.setProperty(getSection(), getPrefix() + name, value);
		config.store();
		
		log("Added " + getSection() + " value " + name + " = " + value);
	}
	
	private void removeProperty(Args args) {
		if (!args.param("name").isPresent()) {
			throw new IllegalArgumentException("You should provide the " + getCommand());
		}
		
		String name = args.param("name").get();
		
		if (!config.containsProperty(name)) {
			throw new IllegalArgumentException(getCommand() + " " + name + " not found");
		}
		
		config.removeProperty(getCommand(), getPrefix() + name);
		config.store();
		
		log(getSection() + " value " + name + " was removed");
	}
	
}
