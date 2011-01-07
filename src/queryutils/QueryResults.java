package queryutils;

import org.apache.lucene.document.Document;
import core.Query;
import core.ScoreComparator;
import java.util.ArrayList;
import java.util.Collections;
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
		List<Entry<Document, Float>> docScoreList = new ArrayList<Entry<Document, Float>>(queryResults.entrySet());
		Collections.sort(docScoreList, new ScoreComparator<Document>());
		Collections.reverse(docScoreList);
		List<Document> mostRelDocs = new ArrayList<Document>(docScoreList.size());
		for (Entry<Document, Float> entry : docScoreList) {
			mostRelDocs.add(entry.getKey());
		}
		return mostRelDocs;
	}
}
