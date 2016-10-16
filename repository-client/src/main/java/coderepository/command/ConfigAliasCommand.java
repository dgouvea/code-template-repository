package coderepository.command;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConfigAliasCommand extends AbstractCommandLine {

	@Override
	protected void run(Args args) {
		if (!args.hasNext()) {
			return;
		}
		
		String param = args.next();
		
		if (!param.equals("config")) {
			return;
		}
		
		if (!args.hasNext()) {
			return;
		}
		
		String action = args.next();
		
		if (action.equals("alias")) {
			if (args.hasNext()) {
				addAlias(args);
			} else {
				showAlias();
			}
		} else if (action.equals("remove-alias")) {
			removeAlias(args);
		}
	}

	private void showAlias() {
		StringBuilder text = new StringBuilder();
		
		String section = "dependency";
		text.append("## ").append(section).append(" aliases").append("\n");
		Map<String, String> properties = config.getSection(section);
		properties.forEach((name, value) -> {
			if (name.startsWith(section + ".alias")) {
				text.append(name.replace(section + ".alias.", "\t")).append(" = ").append(value).append("\n");
			}
		});
		
		log(text.toString());
	}
	
	private void addAlias(Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the alias name and dependency");
		}
		
		String aliasName = args.next();
		
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the dependency");
		}

		String dependency = args.next();
		
		String[] parts = dependency.split("\\:");
		if (parts.length != 3 && parts.length != 4) {
			throw new IllegalArgumentException("Invalid dependency format, the pattern is group:artifact:version or group:artifact:version:scope");
		}
		
		config.setProperty("dependency", "alias." + aliasName, dependency);
		config.store();
		
		log("Added alias " + aliasName + " to " + dependency);
	}
	
	private void removeAlias(Args args) {
		if (!args.hasNext()) {
			throw new IllegalArgumentException("You should provide the dependency alias");
		}
		
		String aliasName = args.next();
		
		config.removeProperty("dependency", "alias." + aliasName);
		config.store();
		
		log("Dependency alias " + aliasName + " was removed");
	}

}
