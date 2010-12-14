package queryutils;

import parsers.StopWordParser;
import cacm.CacmDocument;
import cacm.CacmQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class QuerySearcher {

	private Directory index;
	private Analyzer analyzer;
	private Set<String> stopwords;

	public QuerySearcher(List<CacmDocument> documentList) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.index = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Version.LUCENE_29, stopwords);
		this.analyzer = new SimpleAnalyzer();
		createIndex(documentList);
	}

	public QuerySearcher(List<CacmDocument> documentList, String StopWordFilename) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.index = new RAMDirectory();
		this.stopwords = new StopWordParser(StopWordFilename).parse();
		this.analyzer = new StandardAnalyzer(Version.LUCENE_29, stopwords);
		createIndex(documentList);
	}

	public QuerySearcher(List<CacmDocument> documentList, Analyzer analyzer) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.index = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Version.LUCENE_29, stopwords);
		this.analyzer = analyzer;
		createIndex(documentList);
	}

	private void createIndex(List<CacmDocument> documentList) throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter idxwriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		for (CacmDocument cacmDocument : documentList) {
			Document document = new Document();
			document.add(new Field(CacmDocument.Fields.ABSTRACT, cacmDocument.getAbstractInfo(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.AUTHORS, cacmDocument.getAuthors().toString(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.DATE, cacmDocument.getDate(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ENTRYDATE, cacmDocument.getEntrydate(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ID, cacmDocument.getId(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.REFERENCE, cacmDocument.getReferences().toString(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.KEYWORDS, cacmDocument.getKeywords().toString(),
					       Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.TITLE, cacmDocument.getTitle(),
					       Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			idxwriter.addDocument(document);
		}
		idxwriter.optimize();
		idxwriter.close();
	}

	public Collection<QueryResults> search(List<CacmQuery> queryList, String field, int resultsLimit) throws ParseException, CorruptIndexException, IOException {
		IndexSearcher idxSearcher = new IndexSearcher(index, true);
		QueryParser queryParser = new QueryParser(Version.LUCENE_29, field, analyzer);
		Collection<QueryResults> searchResults = new ArrayList<QueryResults>(queryList.size());
		for (CacmQuery cacmQuery : queryList) {
			TopScoreDocCollector collector = TopScoreDocCollector.create(resultsLimit, true);
			String querystr = QueryUtils.normalizeQuery(cacmQuery.getQuery());
			idxSearcher.search(queryParser.parse(querystr), collector);
			QueryResults queryResults = new QueryResults(cacmQuery, resultsLimit);
			for (ScoreDoc scoreDoc : collector.topDocs().scoreDocs) {
				queryResults.addRelevantDoc(idxSearcher.doc(scoreDoc.doc), scoreDoc.score);
			}
			searchResults.add(queryResults);
		}
		return searchResults;
	}
}
