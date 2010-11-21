package core;

public class SearchResult {

	private String qid;
	private String did;
	private float score;
	private String matchingDocument;
	private String queryText;

	public SearchResult(String qid, String queryText, String did,
			    String documentTitle, float score) {
		this.qid = qid;
		this.did = did;
		this.score = score;
		this.matchingDocument = documentTitle;
		this.queryText = queryText;
	}

	public float getScore() {
		return score;
	}

	@Override
	public String toString() {
		return String.format("%6s - %s\n%6s - %f : %s", qid, queryText,
				     did, score, matchingDocument);
	}

	public String getQid() {
		return qid;
	}

	public String getDid() {
		return did;
	}

	public String getQueryText() {
		return queryText;
	}

	public String getDocumentTitle() {
		return matchingDocument;
	}
}
