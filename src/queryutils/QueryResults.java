package queryutils;

import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.Document;
import core.Query;

public class QueryResults {

	private Query query;
	private Map<Document, Float> relevantDocuments;

	public QueryResults(Query query) {
		this(query, 60);
	}

	public QueryResults(Query query, int resultLimit) {
		this.query = query;
		this.relevantDocuments = new HashMap<Document, Float>(resultLimit);
	}

	public Float addRelevantDoc(Document document, float score) {
		return this.relevantDocuments.put(document, score);
	}

	public Query query() {
		return query;
	}

	public Map<Document, Float> queryResults() {
		return relevantDocuments;
	}
}
