package coderepository;

import org.junit.Assert;
import org.junit.Test;

public class DependencyManagerFactoryTest {

	@Test
	public void testInstance() {
		DependencyManagerFactory factory = new DependencyManagerFactory();
		Assert.assertEquals(2, factory.getDependencyManagers().size());
		Assert.assertEquals(MavenDependencyManager.class, factory.getDependencyManager("Maven").getClass());
		Assert.assertEquals(GradleDependencyManager.class, factory.getDependencyManager("Gradle").getClass());
	}
	
}
