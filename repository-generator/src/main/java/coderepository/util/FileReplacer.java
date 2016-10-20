package coderepository.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileReplacer {

	private static final String FILE_NAME_PATTERN = ".*__(.+)__.*";
	
	public static void replaceFolders(File folder, boolean override, Map<String, String> properties) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			String fileName = file.getName();
			if (fileName.matches(FILE_NAME_PATTERN)) {
				String propertyName = fileName.replaceAll(FILE_NAME_PATTERN, "$1");
				if (properties.containsKey(propertyName)) {
					String value = properties.get(propertyName);
					
					if (file.isDirectory()) {
						File newFoler = new File(folder, value);
						newFoler.mkdirs();
						
						File[] listFiles = file.listFiles();
						for (File subFile : listFiles) {
							moveToDirectory(subFile, newFoler, override);
						}
						
						replaceFolders(newFoler, override, properties);
						file.delete();
					} else {
						File newFile = new File(folder, file.getName().replace("__" + propertyName + "__", value));
						if (newFile.exists()) {
							if (!override) {
								FileUtils.forceDelete(file);
								continue;
							} else {
								FileUtils.forceDelete(newFile);
							}
						}
						file.renameTo(newFile);
						replaceContent(newFile, properties);
					}
				}
			} else if (file.isDirectory()) {
				replaceFolders(file, override, properties);
			} else {
				replaceContent(file, properties);
			}
		}
	}

	private static void moveToDirectory(File src, File destDir, boolean override) throws IOException {
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		if (src.isDirectory()) {
			File[] files = src.listFiles();
			for (File file : files) {
				moveToDirectory(file, new File(destDir, src.getName()), override);
			}
			FileUtils.deleteDirectory(src);
		} else {
			File newFile = new File(destDir, src.getName());
			if (newFile.exists()) {
				if (!override) {
					FileUtils.forceDelete(src);
					return;
				} else {
					FileUtils.forceDelete(newFile);
				}
			}
			FileUtils.moveFileToDirectory(src, destDir, false);
		}
	}

	private static void replaceContent(File file, Map<String, String> properties) throws IOException, FileNotFoundException {
		Charset encoding = Charset.forName("UTF-8");
		
		String data;

		try (FileInputStream inputStream = new FileInputStream(file)) {
			data = IOUtils.toString(inputStream, encoding);
		}
			
		Content content = new Content();
		content.data = data;
		
		properties.entrySet().forEach(e -> {
			content.data = content.data.replace("__" + e.getKey() + "__", e.getValue());
		});
		
		if (!data.equals(content.data)) {
			try (FileOutputStream outputStream = new FileOutputStream(file)) {
				IOUtils.write(content.data, outputStream, encoding);
			}
		}
	}

	private static class Content {
		
		private String data;
		
	}
	
}
