package sk.fiit.martinfranta.disambiguation;

import java.io.Serializable;

public class Position implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3280536323941300874L;
	protected int start;
	protected int end;
	protected int length;
	protected String document;
	
	public Position (int start, int end) {
		this.start = start;
		this.end = end;
		this.length = end - start;
	}
	
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

	public int getEnd() {
		return end;
	}

	public void setEnd(int endIndex) {
		this.end = endIndex;
		
	}
}
