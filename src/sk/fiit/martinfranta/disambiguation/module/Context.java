package sk.fiit.martinfranta.disambiguation.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import sk.fiit.martinfranta.tools.DataSet;

public abstract class Context implements Serializable {

	private static final long serialVersionUID = -4471703371226398143L;
	
	protected Map<String, ContextAttribute<?>> attributes;
	protected Entity entity;
	
	public Map<String, ContextAttribute<?>> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, ContextAttribute<?>> attributes) {
		this.attributes = attributes;
	}

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public Map<String, ContextAttribute<?>> getContextAttributes() {
		return attributes;
	}
	
	public void setContextAttributes(Map<String, ContextAttribute<?>> attributes) {
		this.attributes = attributes;
	}
	
	public Context(Entity e) {
		if (e != null) {
			this.entity = e;
			attributes = new HashMap<String, ContextAttribute<?>>();
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
			 sparql = sparql.replaceAll("\\$"+String.valueOf(i)+"\\$", attributes[i]);
		}
		return sparql;
	}
	
	/** MUST IMPLEMENT */
	public abstract void assignAttributes();
	
	public abstract void setContextResultSet(ContextAttribute<?> attr, DataSet<?> resultSet);
}
