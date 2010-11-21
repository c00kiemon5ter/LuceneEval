package core.trec;

import core.SearchResult;
import java.util.Collection;

public class TrecResults {

	private Collection<SearchResult> results;

	public TrecResults(Collection<SearchResult> results) {
		this.results = results;
	}

	public String trecResultLine(SearchResult searchResult) {
		return String.format("%s\tITER\t%s\tRANK\t%f\tRUN",
				     searchResult.getQid(),
				     searchResult.getDid(),
				     searchResult.getScore());
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		for (SearchResult result : results) {
			strbuf.append(result.getQid()).append("\tITER").
				append(result.getDid()).append("\tRANK").
				append(result.getScore()).append("\tRUN\n");
		}
		return strbuf.toString();
	}
}
