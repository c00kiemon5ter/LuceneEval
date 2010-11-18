package parsers;

import core.CacmDocument;
import core.CacmQuery;
import core.lists.CacmDocumentList;
import core.lists.CacmQueryList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class QuerySearcher {

	private Directory index;
	private Analyzer analyzer;

	public QuerySearcher(CacmDocumentList doclist) throws CorruptIndexException, LockObtainFailedException, IOException {
		this.index = new RAMDirectory();
		this.analyzer = new StandardAnalyzer(Version.LUCENE_29, new File("data/common_words"));
		createIndex(doclist.getDocuments());
	}

	private void createIndex(List<CacmDocument> doclist) throws CorruptIndexException, LockObtainFailedException, IOException {
		IndexWriter idxwriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		Document document;
		for (CacmDocument doc : doclist) {
			document = new Document();
			document.add(new Field(CacmDocument.Fields.ABSTRACT, doc.getAbstractInfo(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.AUTHORS, doc.getAuthors().toString(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.DATE, doc.getDate(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ENTRYDATE, doc.getEntrydate(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.ID, doc.getId(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.REFERENCE, doc.getReferences().toString(),
					       Field.Store.YES, Field.Index.NO, Field.TermVector.NO));
			document.add(new Field(CacmDocument.Fields.KEYWORDS, doc.getKeywords().toString(),
					       Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			document.add(new Field(CacmDocument.Fields.TITLE, doc.getTitle(),
					       Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.YES));
			idxwriter.addDocument(document);
		}
		idxwriter.optimize();
		idxwriter.close();
	}

	public List<TopDocs> search(CacmQueryList querylist) throws ParseException, CorruptIndexException, IOException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_29, CacmDocument.Fields.TITLE, analyzer);
		IndexSearcher idxSearcher = new IndexSearcher(index, true);
		List<TopDocs> topDocs = new ArrayList<TopDocs>(querylist.getQueries().size());
		TopScoreDocCollector collector = null;
		for (CacmQuery cacmQuery : querylist.getQueries()) {
			collector = TopScoreDocCollector.create(10, true);
			String query = normalize(cacmQuery.getQuery());
			idxSearcher.search(queryParser.parse(query), collector);
			topDocs.add(collector.topDocs());
		}
		return topDocs;
	}

	private String normalize(String query) {
		String pattern = "[\\\\|(|)|\"|?|*|\\-|\']";
		return query.replaceAll(pattern, " ");
	}
}
