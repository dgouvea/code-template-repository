package coderepository.command;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import coderepository.command.Options.Args;

@Component
public class ConfigCommand extends AbstractCommandLine {
	
	@Override
	protected String getCommand() {
		return "config";
	}
	
	@Override
	protected String getHelp() {
		return "Manage configurations";
	}
	
	@Override
	public Options options() {
		Options options = new Options();
		options.param(getCommand(), getHelp());
		return options;
	}
	
	@Override
	protected void run(Args args) {
		if (args.params().size() == 1) {
			log(configFileToString());
		}
	}

	private String configFileToString() {
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
