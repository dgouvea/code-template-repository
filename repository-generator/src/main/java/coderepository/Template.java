package coderepository;

import java.util.Date;

public class Template {

	private String name;
	private String fileName;
	private Date date;
	private long size;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "Template [name=" + name + ", fileName=" + fileName + ", date=" + date + ", size=" + size + "]";
	}

}
