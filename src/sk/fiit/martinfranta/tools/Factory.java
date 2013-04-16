package sk.fiit.martinfranta.tools;

import org.apache.log4j.Logger;

import sk.fiit.martinfranta.disambiguation.module.Candidate;
import sk.fiit.martinfranta.disambiguation.module.Context;
import sk.fiit.martinfranta.disambiguation.module.Entity;
import sk.fiit.martinfranta.disambiguation.module.RecordBuilder;

public class Factory {
	
	private static Logger logger = Logger.getLogger(Factory.class);
	
	public static Entity createEntity(String argument) {
		try {
			return (Entity) Class.forName("sk.fiit.martinfranta.disambiguation.module.dblp.AuthorEntity").getDeclaredConstructor(String.class).newInstance(argument);
		} catch (Exception e) {
			logger.info(e, e);
		} 
		return null;
	}
	
	public static Candidate createCandidate(String argument) {
		try {
			return (Candidate) Class.forName("sk.fiit.martinfranta.disambiguation.module.dblp.AuthorCandidate").getDeclaredConstructor(String.class).newInstance(argument);
		} catch (Exception e) {
			logger.info(e, e);
		} 
		return null;
	}
	
	public static Context createContext() {
		try {
			return (Context) Class.forName("DblpContext").newInstance();
		} catch (Exception e) {
			logger.info(e.getStackTrace());
		} 
		return null;
	}

	public static RecordBuilder createRecordBuilder(Entity e) {
		try {
			return (RecordBuilder) Class.forName("sk.fiit.martinfranta.module.dblp.DblpRecordBuilder").getDeclaredConstructor(Entity.class).newInstance(e);
		} catch (Exception ex) {
			logger.info(ex.getStackTrace());
		} 
		return null;
	}
	
	public static IExecutor createExecutor() {
		try {
			return (IExecutor) Class.forName("sk.fiit.martinfranta.tools.sparql.SparqlExecutor").newInstance();
		} catch (Exception e) {
			logger.info(e, e);
		} 
		return null;
	}
	
	
}
