package sk.fiit.martinfranta.disambiguation.module;

import java.io.Serializable;

public class ContextAttribute<T> implements Serializable {

	private static final long serialVersionUID = -7279219176318548028L;
	private T  attribute;
	private String query;
	private String identifier;
	
	public ContextAttribute(T t, String identifier) {
		attribute = t;
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public T getAttribute() {
		return (T) attribute;
	}

	public void setAttribute(T attribute) {
		this.attribute = attribute;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
