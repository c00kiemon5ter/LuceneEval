package core;

public class SearchResult {

	private int qid;
	private int docId;
	private float score;
	private String matchingDocument;
	private String queryText;

	public SearchResult(int qid, String queryText, int did,
			    String documentTitle, float score) {
		this.qid = qid;
		this.docId = did;
		this.score = score;
		this.matchingDocument = documentTitle;
		this.queryText = queryText;
	}

	public float getScore() {
		return score;
	}

	@Override
	public String toString() {
		return String.format("%6d - %s\n%6d - %f : %s", qid, queryText,
				     docId, score, matchingDocument);
	}

	public int getQid() {
		return qid;
	}

	public int getDocId() {
		return docId;
	}

	public String getQueryText() {
		return queryText;
	}

	public String getDocumentTitle() {
		return matchingDocument;
	}
}
