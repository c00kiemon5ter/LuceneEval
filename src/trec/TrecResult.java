package trec;

public class TrecResult {

	private int qid;
	private int did;
	private float score;

	public TrecResult(int qid, int did, float score) {
		this.qid = qid;
		this.did = did;
		this.score = score;
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
