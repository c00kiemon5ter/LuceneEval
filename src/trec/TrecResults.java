package trec;

import cacm.CacmDocument;
import queryutils.QueryResults;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;

public class TrecResults {

	private List<TrecResult> trecResults;

	public TrecResults(List<QueryResults> searchResults) {
		this.trecResults = new ArrayList<TrecResult>(searchResults.size());
		for (QueryResults queryResults : searchResults) {
			for (Document document : queryResults.relevantDocs()) {
				int qid = queryResults.query().getQid();
				int docid = Integer.parseInt(document.get(CacmDocument.Fields.ID));
				float score = queryResults.queryResults().get(document);
				TrecResult trecResult = new TrecResult(qid, docid, score);
				this.trecResults.add(trecResult);
			}
		}
	}

	@Override
	public String toString() {
		return trecResults.toString();
	}

	public void write(String resultsFilename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(resultsFilename)));
		for (TrecResult result : trecResults) {
			writer.write(result.toString());
		}
		writer.flush();
		writer.close();
	}
}
