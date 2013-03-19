package sk.fiit.martinfranta.disambiguation.module;

import org.apache.log4j.Logger;

public class Factory {
	
	private static Logger logger = Logger.getLogger(Factory.class);
	
	public static IEntity createEntity() {
		try {
			return (IEntity) Class.forName("sk.fiit.martinfranta.disambiguation.module.dblp.AuthorEntity").newInstance();
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
	
}
