package coderepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

public abstract class DependencyManager {

	public final void install(File file, List<Dependency> dependencies) {
		Charset encoding = Charset.forName("UTF-8");

		StringBuilder content = new StringBuilder();

		try (FileInputStream inputStream = new FileInputStream(file)) {
			content.append(IOUtils.toString(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		String textDependencies = toString(dependencies);
		
		int position = content.indexOf(getAnchor());
		content.insert(position, textDependencies);
		
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			IOUtils.write(content, outputStream, encoding);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public final String toString(List<Dependency> dependencies) {
		return dependencies.stream().map(dependency -> toString(dependency)).collect(Collectors.joining());
	}
	
	protected abstract String toString(Dependency dependency);

	protected abstract String getAnchor();
	
	public abstract String getSettingFileName();

	public abstract String getName();

}
