package coderepository.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import coderepository.Config;

@Component
public class CommandLineExecutor implements CommandLineRunner {

	@Autowired
	private Config config;
	
	@Autowired
	private VersionCommand versionCommand;
	
	@Autowired
	private ConfigCommand configCommand;
	
	@Autowired
	private ConfigDefaultCommand configDefaultCommand;
	
	@Autowired
	private ConfigAliasCommand configAliasCommand;

	@Autowired
	private ConfigRepositoryCommand configRepositoryCommand;
	
	@Autowired
	private PushCommand pushCommand;

	@Autowired
	private InstallCommand installCommand;
	
	@Autowired
	private HelpCommand helpCommand;

	@Autowired
	private TemplateListCommand templateListCommand;
	
	@Autowired
	private ShareCommand shareCommand;
	
	private final List<AbstractCommandLine> commands = new ArrayList<>();
	
	@PostConstruct
	private void load() {
		commands.add(helpCommand);
		commands.add(versionCommand);
		commands.add(configCommand);
		commands.add(configDefaultCommand);
		commands.add(configAliasCommand);
		commands.add(configRepositoryCommand);
		commands.add(pushCommand);
		commands.add(installCommand);
		commands.add(templateListCommand);
		commands.add(shareCommand);
	}
	
	@Override
	public void run(String... arguments) throws Exception {
		Map<String, String> dependencyAliases = config.getSection("dependency");
		
		commands.forEach(command -> {
			Options options = command.options();
			command.execute(options.parse(dependencyAliases, arguments));
		});
	}

}
