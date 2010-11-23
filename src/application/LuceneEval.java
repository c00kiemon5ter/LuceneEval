package application;

import core.SearchResult;
import java.io.FileNotFoundException;
import org.apache.lucene.store.LockObtainFailedException;
import parsers.QuerySearcher;
import cacm.lists.CacmDocumentList;
import cacm.lists.CacmQueryList;
import trec.TrecQrels;
import trec.TrecResults;
import io.XmlReader;
import io.XmlWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.queryParser.ParseException;
import parsers.CacmDocParser;
import parsers.CacmQrelsParser;
import parsers.CacmQueryParser;
import trec.TrecProcess;

public class LuceneEval {

	/* configuration */
	private static final String DATAFILE = "data/cacm/cacm.all";
	private static final String CACM_XML = "data/results/cacm.all.xml";
	private static final String QUERYFILE = "data/cacm/query.text";
	private static final String STOPWORDLIST = "data/cacm/common_words";
	private static final String CACM_QRELS_FILE = "data/cacm/qrels.text";
	private static final String TREC_QRELS_FILE = "data/results/trec_qrels";
	private static final String TREC_SEARCHRESULTS_FILE = "data/results/trec_searchresults";
	private static final String TREC_RESULTS_FILE = "data/results/trec_results";
	private static final int RESULTS_LIMIT = 20;

	public static void main(String[] args) {
		try {
			LuceneEval luceneEval = new LuceneEval();
		} catch (Exception ex) {
			Logger.getLogger(LuceneEval.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private LuceneEval() throws FileNotFoundException, LockObtainFailedException, IOException, ParseException, Exception {
		System.out.printf("Parsing cacm documents from file: %s\n", DATAFILE);
		CacmDocumentList documentList = new CacmDocumentList(new CacmDocParser(DATAFILE).parse());

		System.out.printf("Writing cacm documents to xml file: %s\n", CACM_XML);
		new XmlWriter<CacmDocumentList>(CACM_XML).write(documentList);

		System.out.printf("Loading cacm documents from xml file: %s\n", CACM_XML);
		documentList = new XmlReader<CacmDocumentList>(CACM_XML, CacmDocumentList.class).read();

		System.out.printf("Parsing cacm queries from file: %s\n", QUERYFILE);
		CacmQueryList queryList = new CacmQueryList(new CacmQueryParser(QUERYFILE).parse());

		System.out.println("Searching cacm documents with cacm queries");
		/* do not use stopwords */
//		List<SearchResult> results = new QuerySearcher(documentList.getDocuments()).search(queryList.getQueries(), RESULTS_LIMIT);
		/* use stopwords */
		List<SearchResult> results = new QuerySearcher(documentList.getDocuments(), STOPWORDLIST).search(queryList.getQueries(), RESULTS_LIMIT);

		/* uncomment to print search results */
//		System.out.println("Printing results to output");
//		printSearchResults(results);

		System.out.printf("Writing trec-formated results to file: %s\n", TREC_SEARCHRESULTS_FILE);
		new TrecResults(results).write(TREC_SEARCHRESULTS_FILE);

		System.out.printf("Parsing cacm qrels from file: %s\n", CACM_QRELS_FILE);
		List<String> cacmqrels = new CacmQrelsParser(CACM_QRELS_FILE).parse();

		System.out.printf("Writing treq-formated qrels to file: %s\n", TREC_QRELS_FILE);
		new TrecQrels(cacmqrels).write(TREC_QRELS_FILE);

		System.out.printf("Evaluating results with %s\n", TrecProcess.TREC_EXECUTABLE);
		int status = new TrecProcess(TREC_QRELS_FILE, TREC_SEARCHRESULTS_FILE, TREC_RESULTS_FILE).run();
		System.out.printf("Evaluation results are in file: %s\n", TREC_RESULTS_FILE);

		System.exit(status);
	}

	private void printSearchResults(Collection<SearchResult> results) {
		String prevQid = "";
		String seperator = "-----------------------------------";
		for (SearchResult result : results) {
			if (!result.getQid().equals(prevQid)) {
				prevQid = result.getQid();
				System.out.format("%s\nSearching for: %s - %s\n%s\n",
						  seperator, result.getQid(),
						  result.getQueryText(), seperator);
			}
			System.out.printf("%6s - %6f\t%s\n", result.getDid(),
					  result.getScore(), result.getDocumentTitle());
		}
	}
}
