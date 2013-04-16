package sk.fiit.martinfranta.tools;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryEvaluationException;


public interface IExecutor {
	public void initialize();
	public DataIterator<?> executeQuery(String query) throws KnowledgeBaseException;
	public void closeResult(DataIterator<?> result) throws Exception;
	public boolean hasNext(DataIterator<?> result) throws Exception;
	BindingSet next(DataIterator<?> result) throws Exception;
}
