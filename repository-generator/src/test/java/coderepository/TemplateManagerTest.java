package coderepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TemplateManagerTest {

	@Before
	public void createOutDir() {
		File out = new File("out");
		if (!out.exists()) {
			out.mkdir();
		}
	}
	
	@After
	public void deleteOutDir() throws IOException {
		FileUtils.deleteDirectory(new File("out"));
	}
	
	@Test
	public void testInstall() throws IOException {
		TemplateManager packageManager = new TemplateManager(new Config(), "templates/project-a");
		packageManager.setGroup("coderepository.template.newproject");
		packageManager.setArtifact("new-project");
		packageManager.setVersion("0.0.1");
		packageManager.setPackaging("jar");
		packageManager.setProperty("main", "MainClass");
		packageManager.install("out");

		packageManager = new TemplateManager(new Config(), "templates/project-b");
		packageManager.setGroup("coderepository.template.newproject");
		packageManager.setArtifact("new-project");
		packageManager.setVersion("0.0.1");
		packageManager.setPackaging("jar");
		packageManager.install("out");
		
		Assert.assertTrue(Files.exists(Paths.get("out/new-project")));
		Assert.assertTrue(Files.exists(Paths.get("out/new-project/src/main/java")));
		Assert.assertTrue(Files.exists(Paths.get("out/new-project/src/main/java/coderepository/template/newproject")));
		Assert.assertFalse(Files.exists(Paths.get("out/new-project/src/main/java/__packageFolder__")));
		Assert.assertTrue(Files.exists(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/ClassB.java")));
		Assert.assertEquals("package coderepository.template.newproject;", Files.readAllLines(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/ClassB.java")).get(0));
		Assert.assertTrue(Files.exists(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/MainClass.java")));
		Assert.assertEquals("package coderepository.template.newproject;", Files.readAllLines(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/MainClass.java")).get(0));
		Assert.assertFalse(Files.exists(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/__main__.java")));
		Assert.assertTrue(Files.exists(Paths.get("out/new-project/src/main/java/coderepository/template/newproject/ClassC.java")));
		
		try (FileInputStream stream = new FileInputStream("out/new-project/src/main/java/coderepository/template/newproject/ClassB.java")) {
			String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
			Assert.assertFalse(content.contains("public void print()"));
		}

		try (FileInputStream stream = new FileInputStream("out/new-project/pom.xml")) {
			String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
			Assert.assertTrue(content.contains("<groupId>coderepository.template.newproject</groupId>"));
			Assert.assertTrue(content.contains("<artifactId>new-project</artifactId>"));
			Assert.assertTrue(content.contains("<version>0.0.1</version>"));
			Assert.assertTrue(content.contains("<packaging>jar</packaging>"));
		}
	}

	@Test
	public void testInstallOverride() throws IOException {
		TemplateManager packageManager = new TemplateManager(new Config(), "templates/project-a");
		packageManager.setGroup("coderepository.template.newproject");
		packageManager.setArtifact("new-project");
		packageManager.setVersion("0.0.1");
		packageManager.setPackaging("jar");
		packageManager.setProperty("main", "MainClass");
		packageManager.install("out");
		
		packageManager = new TemplateManager(new Config(), "templates/project-b");
		packageManager.setGroup("coderepository.template.newproject");
		packageManager.setArtifact("new-project");
		packageManager.setPackaging("jar");
		packageManager.install("out", true);
		
		try (FileInputStream stream = new FileInputStream("out/new-project/src/main/java/coderepository/template/newproject/ClassB.java")) {
			String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
			Assert.assertTrue(content.contains("public void print()"));
		}
	}

	@Test
	public void testInstallDependencies() throws IOException {
		TemplateManager packageManager = new TemplateManager(new Config(), "templates/project-a");
		packageManager.setDependencyManagerSystem("Maven");
		packageManager.setGroup("coderepository.template.newproject");
		packageManager.setArtifact("new-project");
		packageManager.setVersion("0.0.1");
		packageManager.setPackaging("jar");
		packageManager.setProperty("main", "MainClass");
		packageManager.dependency("junit", "junit", "4.12", "test");
		packageManager.install("out");
		/*
		try (FileInputStream stream = new FileInputStream("out/new-project/pom.xml")) {
			String content = IOUtils.toString(stream, Charset.forName("UTF-8"));
			Assert.assertTrue(content.contains("<groupId>junit</groupId>"));
			Assert.assertTrue(content.contains("<artifactId>junit</artifactId>"));
			Assert.assertTrue(content.contains("<version>4.12</version>"));
			Assert.assertTrue(content.contains("<scope>test</scope>"));
		}
		*/
	}
	
}
