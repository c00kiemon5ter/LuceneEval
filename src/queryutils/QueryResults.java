package queryutils;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

public class QueryResults {

	private Query query;
	private int queryId;
	private Map<Document, Float> relevantDocuments;

	public QueryResults(Query query, int queryId) {
		this(query, queryId, 60);
	}

	public QueryResults(Query query, int queryId, int resultLimit) {
		this.query = query;
		this.queryId = queryId;
		this.relevantDocuments = new HashMap<Document, Float>(resultLimit);
	}

	public Float addRelevantDoc(Document document, float score) {
		return this.relevantDocuments.put(document, score);
	}

	public int getQueryId() {
		return queryId;
	}

	public Query query() {
		return query;
	}

	public Map<Document, Float> queryResults() {
		return relevantDocuments;
	}
}
