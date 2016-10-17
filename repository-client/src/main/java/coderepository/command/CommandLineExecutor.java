package coderepository.command;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineExecutor implements CommandLineRunner {

	@Autowired
	private VersionCommand versionCommand;
	
	@Autowired
	private ConfigCommand configCommand;
	
	@Autowired
	private PushCommand pushCommand;

	@Autowired
	private InstallCommand installCommand;
	
	@Autowired
	private HelpCommand helpCommand;

	@Autowired
	private TemplateListCommand templateListCommand;
	
	private final List<AbstractCommandLine> commands = new ArrayList<>();
	
	@PostConstruct
	private void load() {
		commands.add(helpCommand);
		commands.add(versionCommand);
		commands.add(configCommand);
		commands.add(pushCommand);
		commands.add(installCommand);
		commands.add(templateListCommand);
	}
	
	@Override
	public void run(String... arguments) throws Exception {
		Args args = new Args(arguments);
		
		final String action = args.hasNext() ? args.next() : "";

		commands.forEach(command -> {
			command.execute(action, args);
		});
	}

}
