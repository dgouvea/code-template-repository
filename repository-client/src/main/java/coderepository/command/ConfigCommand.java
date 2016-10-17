package coderepository.command;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConfigCommand extends AbstractCommandLine {
	
	@Autowired
	private ConfigAliasCommand aliasCommand;

	@Autowired
	private ConfigDefaultCommand defaultCommand;
	
	@Override
	protected String getCommand() {
		return "config";
	}
	
	@Override
	protected String getHelp() {
		return "Manage configurations";
	}
	
	@Override
	protected void load(List<AbstractCommandLine> commands) {
		commands.add(aliasCommand);
		commands.add(defaultCommand);
	}
	
	@Override
	protected void run(String action, Args args) {
		if (!args.hasNext()) {
			log(printConfig());
		} else {
			next(args.next(), args);
		}
	}

	private String printConfig() {
		StringBuilder text = new StringBuilder();
		
		List<String> sections = config.getSections();
		sections.forEach(section -> {
			text.append("## ").append(section).append("\n");
			Map<String, String> properties = config.getSection(section);
			properties.forEach((name, value) -> {
				text.append(name.replace(section + ".", "\t")).append(" = ").append(value).append("\n");
			});
			text.append("\n");
		});
		
		return text.toString();
	}

}
