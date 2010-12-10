package trec;

import core.SearchResult;

public class TrecResult {

	private int qid;
	private int did;
	private float score;

	public TrecResult(SearchResult searchResult) {
		this.qid = searchResult.getQid();
		this.did = searchResult.getDocId();
		this.score = searchResult.getScore();
	}

	public int getDid() {
		return did;
	}

	public int getQid() {
		return qid;
	}

	public float getScore() {
		return score;
	}

	@Override
	public String toString() {
		return String.format("%d\tITER\t%d\tRANK\t%f\tRUN\n", qid, did, score);
	}
}
