package coderepository.command;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConfigDefaultCommand extends AbstractCommandLine {

	@Override
	protected String getCommand() {
		return "default";
	}
	
	@Override
	protected String getHelp() {
		return "Manage default values";
	}
	
	@Override
	protected boolean canExecute(String action, Args args) {
		return action.equals("default") || action.equals("remove-default");
	}
	
	@Override
	protected void run(String action, Args args) {
		if (action.equals("default")) {
			if (args.hasNext()) {
				addDefault(args);
			} else {
				showDefault();
			}
		} else if (action.equals("remove-default")) {
			removeDefault(args);
		}
	}

	private void showDefault() {
		StringBuilder text = new StringBuilder();
		
		String section = "default";
		text.append("## ").append(section).append("\n");
		Map<String, String> properties = config.getSection(section);
		properties.forEach((name, value) -> {
			text.append(name.replace(section + ".", "\t")).append(" = ").append(value).append("\n");
		});
		
		log(text.toString());
	}

	private void addDefault(Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the name and value");
		}
		
		String name = args.next();
		
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the value");
		}

		String value = args.next();
		
		config.setProperty("default", name, value);
		config.store();
		
		log("Added default value " + name + " = " + value);
	}
	
	private void removeDefault(Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the default name");
		}
		
		String name = args.next();
		
		config.removeProperty("default", name);
		config.store();
		
		log("Default value " + name + " was removed");
	}
	
}
