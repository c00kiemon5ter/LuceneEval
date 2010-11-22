package trec;

import core.SearchResult;

public class TrecResult {

	private String qid;
	private String did;
	private float score;

	public TrecResult(SearchResult searchResult) {
		this.qid = searchResult.getQid();
		this.did = searchResult.getDid();
		this.score = searchResult.getScore();
	}

	public String getDid() {
		return did;
	}

	public String getQid() {
		return qid;
	}

	public float getScore() {
		return score;
	}

	@Override
	public String toString() {
		return String.format("%s\tITER\t%s\tRANK\t%f\tRUN\n", qid, did, score);
	}
}
