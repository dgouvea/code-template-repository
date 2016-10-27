package coderepository.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.utils.IOUtils;

public class CompressorUtils {

	public static void uncompress(File zipFile, File folder) {
		try (FileInputStream inputStream = new FileInputStream(zipFile)) {
			ZipInputStream zip = new ZipInputStream(inputStream);
			ZipEntry entry;
			byte[] buffer = new byte[1024];
			while ((entry = zip.getNextEntry()) != null ) {
				File file = new File(folder, entry.getName());
				
				File entryfolder = file.getParentFile();
				if (!entryfolder.exists()) {
					entryfolder.mkdirs();
				}
				
				try (FileOutputStream fos = new FileOutputStream(file)) {
					int len;
		            while ((len = zip.read(buffer)) > 0) {
		            	fos.write(buffer, 0, len);
		            }
				}
			}
			zip.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static byte[] compressToBytes(File... files) {
		return compressToBytes(Arrays.asList(files));
	}
	
	public static byte[] compressToBytes(List<File> files) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			compress(outputStream, files);
			return outputStream.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static void compress(File zipFile, File... files) {
		compress(zipFile, Arrays.asList(files));
	}
	
	public static void compress(File zipFile, List<File> files) {
		try (FileOutputStream outputStream = new FileOutputStream(zipFile)) {
			compress(outputStream, files);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private static void compress(OutputStream outputStream, List<File> files) throws IOException {
		ZipOutputStream out = new ZipOutputStream(outputStream);
		files.forEach(file -> {
			if (file.isDirectory()) {
				compressDirectory(file, "", out);
			} else {
				compressFile(file, "", out);
			}
		});
		out.close();
	}

	private static void compressDirectory(File folder, String sourceDir, ZipOutputStream out) {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				compressDirectory(file, sourceDir + File.separator + file.getName(), out);
			} else {
				compressFile(file, sourceDir, out);
			}
		}
	}

	private static void compressFile(File file, String sourceDir, ZipOutputStream out) {
		try {
			String fileName = sourceDir + File.separator + file.getName();
			if (fileName.startsWith(File.separator)) {
				fileName = fileName.substring(1);
			}
			
			ZipEntry entry = new ZipEntry(fileName);
	        out.putNextEntry(entry);
	
	        try (FileInputStream in = new FileInputStream(file)) {
	        	IOUtils.copy(in, out);
	        }
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
