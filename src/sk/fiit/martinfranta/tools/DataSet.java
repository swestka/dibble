package sk.fiit.martinfranta.tools;

public class DataSet<T> {
	private T  dataSet;
	
	public DataSet(T t) {
		setDataSet(t);
	}

	public T getDataSet() {
		return dataSet;
	}

	public void setDataSet(T dataSet) {
		this.dataSet = dataSet;
	}
	
	public String inspect() {
		return dataSet.getClass().getName();
	}
}
