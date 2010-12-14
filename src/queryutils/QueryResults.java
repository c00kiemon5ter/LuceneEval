package queryutils;

import cacm.CacmQuery;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.Document;

public class QueryResults {

	private CacmQuery query;
	private Map<Document, Float> relevantDocuments;

	public QueryResults(CacmQuery query, int resultLimit) {
		this.query = query;
		this.relevantDocuments = new HashMap<Document, Float>(resultLimit);
	}

	public Float addRelevantDoc(Document document, float score) {
		return this.relevantDocuments.put(document, score);
	}

	public CacmQuery query() {
		return query;
	}

	public Map<Document, Float> queryResults() {
		return relevantDocuments;
	}
}
