package coderepository;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DependencyManagerFactoryTest.class, GradleDependencyManagerTest.class, MavenDependencyManagerTest.class,
		TemplateManagerTest.class })
public class AllTests {

}
