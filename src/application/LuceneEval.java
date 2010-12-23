package application;

import cacm.CacmDocument;
import cacm.CacmQuery;
import java.io.FileNotFoundException;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import queryutils.QuerySearcher;
import cacm.lists.CacmDocumentList;
import cacm.lists.CacmQueryList;
import core.Query;
import queryutils.QueryResults;
import trec.TrecQrels;
import trec.TrecResults;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import parsers.CacmDocParser;
import parsers.CacmQrelsParser;
import parsers.CacmQueryParser;
import parsers.StopWordParser;
import core.Utils;
import io.XmlReader;
import io.XmlWriter;
import java.util.List;
import rocchio.QueryExpander;
import rocchio.RocchioExpander;
import trec.TrecProcess;

public class LuceneEval {

	/* configuration */
	/** input files */
	private static final String DATAFILE = "data/cacm/cacm.all";
	private static final String QUERYFILE = "data/cacm/query.text";
	private static final String STOPWORDLIST = "data/cacm/common_words";
	private static final String CACM_QRELS_FILE = "data/cacm/qrels.text";
	/** output files */
	private static final String CACM_XML = "data/results/cacm.all.xml";
	private static final String TREC_QRELS_FILE = "data/results/trec_qrels";
	private static final String TREC_CACMQUERIES_SEARCHRESULTS_FILE = "data/results/trec_cacm_searchresults";
	private static final String TREC_CACM_RESULTS_FILE = "data/results/trec_cacm_results";
	private static final String TREC_ROCCHIOQUERIES_SEARCHRESULTS_FILE = "data/results/trec_rocchio_searchresults";
	private static final String TREC_ROCCHIO_RESULTS_FILE = "data/results/trec_rocchio_results";
	/** search limits */
	private static final int RESULTS_LIMIT = 40;
	private static final int ROCCHIO_DOC_LIMIT = 0;
	private static final int ROCCHIO_EXTRA_TERMS = 0;
	/** analyzation type and fields */
	private final Set<String> stopwords;
	private final Analyzer analyzer;
	private final String searchField = CacmDocument.Fields.TITLE;
	/* End of configuration */

	public static void main(String[] args) {
		try {
			int status;
			LuceneEval luceneEval = new LuceneEval();
			Collection<Document> documents = luceneEval.loadCacmCollection();
			Collection<Query> cacmQueryList = luceneEval.loadCacmQueries();
			luceneEval.loadQrels();
			List<QueryResults> cacmQueriesResults = luceneEval.searchQueriesInDocuments(cacmQueryList, documents);
			luceneEval.writeTrecResults(cacmQueriesResults, TREC_CACMQUERIES_SEARCHRESULTS_FILE);
			status = luceneEval.evaluate(TREC_QRELS_FILE, TREC_CACMQUERIES_SEARCHRESULTS_FILE, TREC_CACM_RESULTS_FILE);
			assert status == 0 : String.format("==> Error: %s: exit status %d", TrecProcess.TREC_EXECUTABLE, status);
			Collection<Query> rocchioQueryList = luceneEval.expandQueries(cacmQueriesResults);
			List<QueryResults> rocchioQueriesResults = luceneEval.searchQueriesInDocuments(rocchioQueryList, documents);
			luceneEval.writeTrecResults(rocchioQueriesResults, TREC_ROCCHIOQUERIES_SEARCHRESULTS_FILE);
			status = luceneEval.evaluate(TREC_QRELS_FILE, TREC_ROCCHIOQUERIES_SEARCHRESULTS_FILE, TREC_ROCCHIO_RESULTS_FILE);
			assert status == 0 : String.format("==> Error: %s: exit status %d", TrecProcess.TREC_EXECUTABLE, status);
		} catch (Exception ex) {
			Logger.getLogger(LuceneEval.class.getName()).log(Level.SEVERE, "==> Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private LuceneEval() throws FileNotFoundException, IOException {
		stopwords = new StopWordParser(STOPWORDLIST).parse();
		analyzer = new StandardAnalyzer(Version.LUCENE_29, stopwords);
	}

	private Collection<Document> loadCacmCollection() throws FileNotFoundException, IOException, Exception {
		System.out.printf(":: Parsing cacm documents from file: %s\n", DATAFILE);
		CacmDocumentList cacmDocumentList = new CacmDocumentList(new CacmDocParser(DATAFILE).parse());

//		System.out.printf(":: Writing cacm documents to xml file: %s\n", CACM_XML);
//		new XmlWriter<CacmDocumentList>(CACM_XML).write(cacmDocumentList);
//		System.out.printf(":: Loading cacm documents from xml file: %s\n", CACM_XML);
//		cacmDocumentList = new XmlReader<CacmDocumentList>(CACM_XML, CacmDocumentList.class).read();

		System.out.printf(":: Producing Lucene documents from cacm documents");
		Collection<Document> documentList = new ArrayList<Document>(cacmDocumentList.getDocuments().size());
		for (CacmDocument cacmDocument : cacmDocumentList.getDocuments()) {
			documentList.add(Utils.convertToLDoc(cacmDocument));
		}
		return documentList;
	}

	private Collection<Query> loadCacmQueries() throws FileNotFoundException, IOException, ParseException {
		System.out.printf(":: Parsing cacm queries from file: %s\n", QUERYFILE);
		CacmQueryList cacmQueryList = new CacmQueryList(new CacmQueryParser(QUERYFILE).parse());

		System.out.printf(":: Producing Lucene queries from cacm queries");
		Collection<Query> queryList = new ArrayList<Query>(cacmQueryList.getQueries().size());
		for (CacmQuery cacmQuery : cacmQueryList.getQueries()) {
			queryList.add(new Query(cacmQuery.getId(), Utils.normalizeQuery(cacmQuery.getQuery(),
											searchField, analyzer)));
		}
		return queryList;
	}

	private List<QueryResults> searchQueriesInDocuments(Collection<Query> queries, Collection<Document> documents)
		throws ParseException, CorruptIndexException, IOException {
		System.out.println(":: Searching queries in documents");
		return new QuerySearcher(documents, analyzer).search(queries, searchField, RESULTS_LIMIT);
	}

	private void writeTrecResults(List<QueryResults> queriesResults, String trecSearchResultsFile) throws IOException {
		System.out.printf(":: Writing trec-formated results to file: %s\n", trecSearchResultsFile);
		new TrecResults(queriesResults).write(trecSearchResultsFile);
	}

	private void loadQrels() throws IOException {
		System.out.printf(":: Parsing cacm qrels from file: %s\n", CACM_QRELS_FILE);
		Collection<String> cacmqrels = new CacmQrelsParser(CACM_QRELS_FILE).parse();

		System.out.printf(":: Writing treq-formated qrels to file: %s\n", TREC_QRELS_FILE);
		new TrecQrels(cacmqrels).write(TREC_QRELS_FILE);
	}

	private int evaluate(String qrelsFile, String searchResultsFile, String trecResultsFile)
		throws FileNotFoundException, IOException, InterruptedException {
		System.out.printf(":: Evaluating results with %s\n", TrecProcess.TREC_EXECUTABLE);
		int status = new TrecProcess(qrelsFile, searchResultsFile, trecResultsFile).run();
		System.out.printf("==> Evaluation results are in file: %s\n", TREC_CACM_RESULTS_FILE);
		if (status != 0) {
			Logger.getLogger(LuceneEval.class.getName()).log(Level.WARNING, String.format(
				"==> Error: %s: exit status %d", TrecProcess.TREC_EXECUTABLE, status));
		}
		return status;
	}

	private Collection<Query> expandQueries(Collection<QueryResults> queriesResults)
		throws ParseException, CorruptIndexException, LockObtainFailedException, IOException {
		System.out.printf(":: Producing Rocchio relevance feedback queries: k=%d a=%.3f b=%.3f c=%.3f\n",
				  ROCCHIO_DOC_LIMIT, QueryExpander.ALPHA,
				  QueryExpander.BETA, QueryExpander.GAMMA);
		Collection<Query> rocchioQueries = new ArrayList<Query>(queriesResults.size());
		QueryExpander expander = new RocchioExpander(analyzer, searchField,
							     ROCCHIO_DOC_LIMIT,
							     ROCCHIO_EXTRA_TERMS);
		for (QueryResults queryResults : queriesResults) {
			Query rocchioQuery = expander.expand(queryResults.query(), queryResults.relevantDocs());
			rocchioQueries.add(rocchioQuery);
		}
		return rocchioQueries;
	}
}
