package sk.fiit.martinfranta.disambiguation.module;

import java.util.HashMap;
import java.util.Map;

public abstract class Context {
	protected Map<String, ContextAttribute<?>> attributes;
	protected IEntity entity;
	
	public Map<String, ContextAttribute<?>> getContextAttributes() {
		return attributes;
	}
	
	public void setContextAttributes(Map<String, ContextAttribute<?>> attributes) {
		this.attributes = attributes;
	}
	
	protected abstract void assignAttributes();
	
	public Context(IEntity e) {
		if (e != null) {
			this.entity = e;
			attributes = new HashMap<String, ContextAttribute<?>>();
			assignAttributes();
		}
	}
	
	public void addAttribute(ContextAttribute<?> attribute) {
		attributes.put(attribute.getIdentifier(), attribute);
	}
	
	public <T> void setAttribute(String identifier, T attribute) {
		ContextAttribute<T> contextAttribute = new ContextAttribute<T>(attribute, identifier);
		attributes.put(identifier, contextAttribute);
	}
	
	public <T> Object getAttribute(String identifier) {
		return attributes.get(identifier).getAttribute();
	}
	
	protected String processQuery(String... attributes) {
		String sparql = attributes[0];
		for (int i = 1; i < attributes.length; i++) {
			 sparql = sparql.replaceAll("\\$"+String.valueOf(i)+"\\$", attributes[1]);
		}
		return sparql;
	}
}
