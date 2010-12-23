package queryutils;

import core.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class QuerySearcher {

	private Directory index;
	private Analyzer analyzer;

	public QuerySearcher(Collection<Document> documents, Analyzer analyzer)
		throws CorruptIndexException, IOException {
		this.index = new RAMDirectory();
		this.analyzer = analyzer;
		createIndex(documents);
	}

	private void createIndex(Collection<Document> documents)
		throws CorruptIndexException, IOException {
		IndexWriter idxwriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		for (Document document : documents) {
			idxwriter.addDocument(document);
		}
		idxwriter.optimize();
		idxwriter.close();
	}

	public List<QueryResults> search(Collection<Query> queryList, String field, int resultsLimit)
		throws ParseException, CorruptIndexException, IOException {
		IndexSearcher idxSearcher = new IndexSearcher(index, true);
		List<QueryResults> searchResults = new ArrayList<QueryResults>(queryList.size());
		for (Query query : queryList) {
			TopScoreDocCollector collector = TopScoreDocCollector.create(resultsLimit, true);
			idxSearcher.search(query.getQuery(), collector);
			QueryResults queryResults = new QueryResults(query, resultsLimit);
			for (ScoreDoc scoreDoc : collector.topDocs().scoreDocs) {
				queryResults.addResult(idxSearcher.doc(scoreDoc.doc), scoreDoc.score);
			}
			searchResults.add(queryResults);
		}
		return searchResults;
	}
}
