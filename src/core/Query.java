package core;

public class Query {
	private int qid;
	private org.apache.lucene.search.Query query;

	public Query(int qid, org.apache.lucene.search.Query query) {
		this.qid = qid;
		this.query = query;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public int getQid() {
		return qid;
	}

	public org.apache.lucene.search.Query getQuery() {
		return query;
	}
}
