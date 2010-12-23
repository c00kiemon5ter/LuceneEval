package application;

import cacm.CacmDocument;
import java.io.FileNotFoundException;
import org.apache.lucene.store.LockObtainFailedException;
import queryutils.QuerySearcher;
import cacm.lists.CacmDocumentList;
import cacm.lists.CacmQueryList;
import queryutils.QueryResults;
import trec.TrecQrels;
import trec.TrecResults;
import io.XmlReader;
import io.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import parsers.CacmDocParser;
import parsers.CacmQrelsParser;
import parsers.CacmQueryParser;
import parsers.StopWordParser;
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
	private static final String TREC_SEARCHRESULTS_FILE = "data/results/trec_searchresults";
	private static final String TREC_RESULTS_FILE = "data/results/trec_results";
	/** search limits */
	private static final int RESULTS_LIMIT = 40;
	private static final int ROCCHIO_DOC_LIMIT = 20;
	private static final int ROCCHIO_EXTRA_TERMS = 10;
	/* End of configuration */

	public static void main(String[] args) {
		try {
			LuceneEval luceneEval = new LuceneEval();
		} catch (Exception ex) {
			Logger.getLogger(LuceneEval.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
		System.exit(0);
	}

	private LuceneEval() throws FileNotFoundException, LockObtainFailedException,
				    IOException, ParseException, Exception {
		final String searchField = CacmDocument.Fields.TITLE;
		Set<String> stopwords = new StopWordParser(STOPWORDLIST).parse();
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_29, stopwords);

		System.out.printf("Parsing cacm documents from file: %s\n", DATAFILE);
		CacmDocumentList documentList = new CacmDocumentList(new CacmDocParser(DATAFILE).parse());

//		System.out.printf("Writing cacm documents to xml file: %s\n", CACM_XML);
//		new XmlWriter<CacmDocumentList>(CACM_XML).write(documentList);
//
//		System.out.printf("Loading cacm documents from xml file: %s\n", CACM_XML);
//		documentList = new XmlReader<CacmDocumentList>(CACM_XML, CacmDocumentList.class).read();

		System.out.printf("Parsing cacm queries from file: %s\n", QUERYFILE);
		CacmQueryList queryList = new CacmQueryList(new CacmQueryParser(QUERYFILE).parse());

		System.out.println("Searching cacm documents with cacm queries");
		Collection<QueryResults> queriesResults = new QuerySearcher(
			documentList.getDocuments(), analyzer).search(
			queryList.getQueries(), searchField, RESULTS_LIMIT);

		System.out.printf("Writing trec-formated results to file: %s\n", TREC_SEARCHRESULTS_FILE);
		new TrecResults(queriesResults).write(TREC_SEARCHRESULTS_FILE);

		System.out.printf("Parsing cacm qrels from file: %s\n", CACM_QRELS_FILE);
		List<String> cacmqrels = new CacmQrelsParser(CACM_QRELS_FILE).parse();

		System.out.printf("Writing treq-formated qrels to file: %s\n", TREC_QRELS_FILE);
		new TrecQrels(cacmqrels).write(TREC_QRELS_FILE);

		System.out.printf("Evaluating results with %s\n", TrecProcess.TREC_EXECUTABLE);
		int status = new TrecProcess(TREC_QRELS_FILE, TREC_SEARCHRESULTS_FILE, TREC_RESULTS_FILE).run();
		System.out.printf("Evaluation results are in file: %s\n", TREC_RESULTS_FILE);
		if (status != 0) {
			Logger.getLogger(LuceneEval.class.getName()).log(Level.WARNING, String.format(
				"Error: %s: exit status %d", TrecProcess.TREC_EXECUTABLE, status));
		}

		System.out.printf("Producing Rocchio relevance feedback queries: k=%d a=%.3f b=%.3f c=%.3f\n",
				  ROCCHIO_DOC_LIMIT, QueryExpander.ALPHA,
				  QueryExpander.BETA, QueryExpander.GAMMA);
		Collection<Query> rocchioQueries = new ArrayList<Query>(queriesResults.size());
		QueryExpander expander = new RocchioExpander(analyzer, searchField,
							     ROCCHIO_DOC_LIMIT,
							     ROCCHIO_EXTRA_TERMS);
		for (QueryResults queryResults : queriesResults) {
			rocchioQueries.add(expander.expand(queryResults.query(),
							   queryResults.queryResults().keySet()));
		}
	}
}
