package queryutils;

import cacm.CacmDocument;
import cacm.CacmQuery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;

public class QuerySearcher {

	private Directory index;
	private Analyzer analyzer;

	public QuerySearcher(List<CacmDocument> documentList, Analyzer analyzer)
		throws CorruptIndexException, LockObtainFailedException, IOException {
		this.index = new RAMDirectory();
		this.analyzer = analyzer;
		createIndex(documentList);
	}

	private void createIndex(List<CacmDocument> documentList)
		throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter idxwriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		for (CacmDocument cacmDocument : documentList) {
			Document document = new Document();
			document.add(new Field(CacmDocument.Fields.ABSTRACT,
					       cacmDocument.getAbstractInfo(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.AUTHORS,
					       cacmDocument.getAuthors().toString(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.DATE,
					       cacmDocument.getDate(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ENTRYDATE,
					       cacmDocument.getEntrydate(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ID,
					       String.valueOf(cacmDocument.getId()),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.REFERENCE,
					       cacmDocument.getReferences().toString(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.KEYWORDS,
					       cacmDocument.getKeywords().toString(),
					       Field.Store.YES, Field.Index.NO,
					       Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.TITLE,
					       cacmDocument.getTitle(),
					       Field.Store.YES, Field.Index.ANALYZED,
					       Field.TermVector.YES));
			idxwriter.addDocument(document);
		}
		idxwriter.optimize();
		idxwriter.close();
	}

	public Collection<QueryResults> search(Collection<core.Query> queryList,
					       String field, int resultsLimit)
		throws ParseException, CorruptIndexException, IOException {
		IndexSearcher idxSearcher = new IndexSearcher(index, true);
		Collection<QueryResults> searchResults = new ArrayList<QueryResults>(queryList.size());
		for (core.Query query : queryList) {
			TopScoreDocCollector collector = TopScoreDocCollector.create(resultsLimit, true);
			idxSearcher.search(query.getQuery(), collector);
			QueryResults queryResults = new QueryResults(query, resultsLimit);
			for (ScoreDoc scoreDoc : collector.topDocs().scoreDocs) {
				queryResults.addRelevantDoc(idxSearcher.doc(scoreDoc.doc), scoreDoc.score);
			}
			searchResults.add(queryResults);
		}
		return searchResults;
	}
}
