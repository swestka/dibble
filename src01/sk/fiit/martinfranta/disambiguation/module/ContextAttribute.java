package sk.fiit.martinfranta.disambiguation.module;

public class ContextAttribute<T> {
	private T  attribute;
	private String sparql;
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

	public String getSparql() {
		return sparql;
	}

	public void setSparql(String sparql) {
		this.sparql = sparql;
	}
}
