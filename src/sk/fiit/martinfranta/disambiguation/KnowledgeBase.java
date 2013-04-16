package sk.fiit.martinfranta.disambiguation;

import sk.fiit.martinfranta.tools.DataIterator;
import sk.fiit.martinfranta.tools.DataSet;
import sk.fiit.martinfranta.tools.Factory;
import sk.fiit.martinfranta.tools.IExecutor;
import sk.fiit.martinfranta.tools.KnowledgeBaseException;

public class KnowledgeBase {
	
	IExecutor executor;
	private static KnowledgeBase kbInstance = new KnowledgeBase();
	
	public DataIterator<?> getData(String query) {
		try {
			return executor.executeQuery(query);
		} catch (KnowledgeBaseException e) {
			return null;
		}
	}

	public static KnowledgeBase getInstance() {
		return kbInstance;
	}

	public void initialize() {
		executor = Factory.createExecutor();
		executor.initialize();
	}

	public boolean hasNext(DataIterator<?> result) {
		try {
			return executor.hasNext(result);
		} catch (Exception e) {	
			throw new RuntimeException(new KnowledgeBaseException(e));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public DataSet<?> next(DataIterator<?> result) {
		try {
			return new DataSet(executor.next(result));
		} catch (Exception e) {
			throw new RuntimeException(new KnowledgeBaseException(e));
		}
	}
	
	public void closeResult(DataIterator<?> result) throws KnowledgeBaseException {
		try {
			executor.closeResult(result);
		} catch (Exception e) {
			throw new KnowledgeBaseException(e);
		}
	}
}

