package core.trec;

import core.SearchResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class TrecResults {

	private Collection<TrecResult> trecResults;

	public TrecResults(Collection<SearchResult> searchResults) {
		this.trecResults = new ArrayList<TrecResult>(searchResults.size());
		TrecResult trecResult;
		for (SearchResult searchResult : searchResults) {
			trecResult = new TrecResult(searchResult);
			this.trecResults.add(trecResult);
		}
	}

	@Override
	public String toString() {
		return trecResults.toString();
	}

	public void write(String resultsFilename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(resultsFilename)));
		writer.write(trecResults.toString());
	}
}
