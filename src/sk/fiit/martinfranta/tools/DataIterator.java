package sk.fiit.martinfranta.tools;

import org.apache.log4j.Logger;


public class DataIterator<T> {
	
	private final T iterator;
	private static Logger logger = Logger.getLogger("DataIterator");
	
	public DataIterator(T t) {
		iterator = t;
	}

	public <T> T getIterator() {
		return (T) iterator;
	}

}
