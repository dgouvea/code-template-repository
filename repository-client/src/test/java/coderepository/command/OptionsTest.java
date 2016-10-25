package coderepository.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import coderepository.Dependency;
import coderepository.command.Options.Args;
import coderepository.command.Options.Option;

public class OptionsTest {

	@Test
	public void testOneParameter() {
		String[] data = new String[]{"one"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(1, args.params().size());
	}

	@Test
	public void testManyParameter() {
		String[] data = new String[]{"one", "two", "three"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		options.param("second", "the second parameter");
		options.param("third", "the third parameter");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");
		params.put("second", "two");
		params.put("third", "three");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals("two", args.param("second").get());
		Assert.assertEquals("three", args.param("third").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(3, args.params().size());
	}
	
	@Test
	public void testOneParameterWithOption() {
		String[] data = new String[]{"one", "-o"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		options.option("override all", "override", "o");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");

		Map<String, String> opts = new HashMap<>();
		opts.put("override", "");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(1, args.params().size());

		Assert.assertTrue(args.hasOption("override"));
		Assert.assertEquals("", args.option("override").get());
		Assert.assertEquals(opts, args.options());
		Assert.assertEquals(1, args.options().size());
	}

	@Test
	public void testWithOptionOneParameter() {
		String[] data = new String[]{"-o", "one"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		options.option("override all", "override", "o");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");
		
		Map<String, String> opts = new HashMap<>();
		opts.put("override", "");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(1, args.params().size());
		
		Assert.assertTrue(args.hasOption("override"));
		Assert.assertEquals("", args.option("override").get());
		Assert.assertEquals(opts, args.options());
		Assert.assertEquals(1, args.options().size());
	}
	
	@Test
	public void testOneParameterWithOptionValue() {
		String[] data = new String[]{"one", "-status", "ok"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		options.option("machine state", true, "status", "s");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");

		Map<String, String> opts = new HashMap<>();
		opts.put("status", "ok");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(1, args.params().size());

		Assert.assertTrue(args.hasOption("status"));
		Assert.assertEquals("ok", args.option("status").get());
		Assert.assertEquals(opts, args.options());
		Assert.assertEquals(1, args.options().size());
	}

	@Test
	public void testOneParameterWithDependencies() {
		String[] data = new String[]{"one", "+junit:junit:4.12:test", "+commons-io:commons-io:2.5"};
		
		Options options = new Options();
		options.param("first", "the first parameter");
		Args args = options.parse(data);
		
		Map<String, String> params = new HashMap<>();
		params.put("first", "one");
		
		Assert.assertEquals("one", args.param("first").get());
		Assert.assertEquals(params, args.params());
		Assert.assertEquals(1, args.params().size());
		
		List<Dependency> dependencies = new ArrayList<>();
		dependencies.add(new Dependency("junit", "junit", "4.12", "test"));
		dependencies.add(new Dependency("commons-io", "commons-io", "2.5"));
		
		Assert.assertEquals(2, args.dependencies().size());
		Assert.assertEquals(dependencies, args.dependencies());
	}

	@Test
	public void testHelp() {
		Options options = new Options();
		options.param("first", "the first parameter");
		options.option("machine state", true, "status", "s");
		
		Assert.assertEquals("first", options.getParameter(0).getName());
		Assert.assertEquals("the first parameter", options.getParameter(0).getHelp());
		
		Option option = options.getOption("status");
		Assert.assertEquals("status", option.getName());
		Assert.assertEquals("machine state", option.getHelp());
		Assert.assertTrue(option.getOptions().contains("status"));
		Assert.assertTrue(option.getOptions().contains("s"));
	}
	
}
