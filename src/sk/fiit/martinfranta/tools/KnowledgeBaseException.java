package sk.fiit.martinfranta.tools;

public class KnowledgeBaseException extends Exception {
	private static final long serialVersionUID = 1L;

	public KnowledgeBaseException(String message) {
		super(message);
	}

	public KnowledgeBaseException(Exception e) {
		super(e);
	}
}
