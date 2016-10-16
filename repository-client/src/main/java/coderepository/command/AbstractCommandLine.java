package coderepository.command;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import coderepository.Config;

public abstract class AbstractCommandLine implements CommandLineRunner {

	@Autowired
	protected Config config;
	
	@Override
	public final void run(String... args) throws Exception {
		try {
			run(new Args(args));
		} catch (Throwable e) {
			e.printStackTrace();
			log(e.getMessage());
		}
	}

	protected final void log(String text) {
		System.out.println(text);
	}
	
	protected abstract void run(Args args);
	
	public static final class Args implements Iterable<String> {
		
		private final List<String> args;
		private final Iterator<String> iterator;

		private Args(String... args) {
			this.args = Arrays.asList(args);
			this.iterator = this.args.iterator();
		}
		
		public boolean hasNext() {
			return iterator.hasNext();
		}

		public String next() {
			return iterator.next();
		}

		@Override
		public Iterator<String> iterator() {
			return args.iterator();
		}
		
	}
	
}
