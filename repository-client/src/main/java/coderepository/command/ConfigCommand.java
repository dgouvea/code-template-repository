package coderepository.command;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ConfigCommand extends AbstractCommandLine {
	
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
			log(printConfig());
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
