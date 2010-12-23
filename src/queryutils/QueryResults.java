package queryutils;

import org.apache.lucene.document.Document;
import core.Query;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class QueryResults {

	private Query query;
	private Map<Document, Float> queryResults;

	public QueryResults(Query query) {
		this(query, 60);
	}

	public QueryResults(Query query, int resultLimit) {
		this.query = query;
		this.queryResults = new HashMap<Document, Float>(resultLimit);
	}

	public Float addResult(Document document, float score) {
		return this.queryResults.put(document, score);
	}

	public Query query() {
		return query;
	}

	public Map<Document, Float> queryResults() {
		return queryResults;
	}

	public List<Document> relevantDocs() {
		List<Entry<Document, Float>> resultsList = new ArrayList<Entry<Document, Float>>(queryResults.entrySet());
		Collections.sort(resultsList, new Comparator<Entry<Document, Float>>() {

			@Override
			public int compare(Entry<Document, Float> a, Entry<Document, Float> b) {
				return a.getValue().compareTo(b.getValue());
			}
		});
		List<Document> relevantDocs = new ArrayList<Document>(resultsList.size());
		for (Entry<Document, Float> entry : resultsList) {
			relevantDocs.add(entry.getKey());
		}
		return relevantDocs;
	}
}
