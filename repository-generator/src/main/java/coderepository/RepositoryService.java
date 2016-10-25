package coderepository;

public interface RepositoryService {

	Templates list();

	boolean exists(String templateName);

	byte[] download(String templateName);

	Template push(String templateName, byte[] bytes);
	
}
