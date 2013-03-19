package sk.fiit.martinfranta.disambiguation;

public class Position {
	
	protected int start;
	protected int length;
	protected String document;
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public String getDocument() {
		return document;
	}
	
	public void setDocument(String document) {
		this.document = document;
	}
}
