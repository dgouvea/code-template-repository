package coderepository.command;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public abstract class ConfigPropertyCommand extends AbstractCommandLine {

	protected abstract String getSection();

	protected String getPrefix() {
		return "";
	}
	
	protected void validate(String name, String value) {
		
	}
	
	@Override
	protected boolean canExecute(String action, Args args) {
		return action.equals(getCommand()) || action.equals("remove-" + getCommand());
	}
	
	@Override
	protected void run(String action, Args args) {
		if (action.equals(getCommand())) {
			if (args.hasNext()) {
				addProperty(args);
			} else {
				showProperties();
			}
		} else if (action.equals("remove-" + getCommand())) {
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
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the name and value");
		}
		
		String name = args.next();
		
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the value");
		}

		String value = args.next();
		
		validate(name, value);
		
		config.setProperty(getSection(), getPrefix() + name, value);
		config.store();
		
		log("Added " + getSection() + " value " + name + " = " + value);
	}
	
	private void removeProperty(Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the " + getCommand());
		}
		
		String name = args.next();
		
		config.removeProperty(getCommand(), getPrefix() + name);
		config.store();
		
		log(getSection() + " value " + name + " was removed");
	}
	
}
