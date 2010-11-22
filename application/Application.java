package application;

import core.SearchResult;
import java.io.FileNotFoundException;
import org.apache.lucene.store.LockObtainFailedException;
import parsers.QuerySearcher;
import core.lists.CacmDocumentList;
import core.lists.CacmQueryList;
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

public class Application {

	/* configuration */
	private static final String DATAFILE = "data/cacm.all";
	private static final String QUERYFILE = "data/query.text";
	private static final String STOPWORDLIST = "data/common_words";
	private static final String CACM_QRELS_FILE = "data/qrels.text";
	private static final String TREC_QRELS_FILE = "data/trec_qrels";
	private static final String TREC_SEARCHRESULTS_FILE = "data/trec_search_results";
	private static final String TREC_RESULTS_FILE = "data/trec_results";
	private static final int RESULTS_LIMIT = 20;

	public static void main(String[] args) {
		try {
			Application app = new Application(DATAFILE, QUERYFILE);
		} catch (Exception ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private Application(String documentsFilename, String queriesFilename) throws FileNotFoundException, LockObtainFailedException, IOException, ParseException, Exception {
		System.out.println("Parsing cacm documents");
		CacmDocumentList doclist = new CacmDocumentList(new CacmDocParser(documentsFilename).parse());

		System.out.println("Writing cacm documents to xml");
		new XmlWriter<CacmDocumentList>(documentsFilename.concat(".xml")).write(doclist);

		System.out.println("Loading cacm documents from xml");
		doclist = new XmlReader<CacmDocumentList>(documentsFilename.concat(".xml"), CacmDocumentList.class).read();

		System.out.println("Parsing cacm queries");
		CacmQueryList querylist = new CacmQueryList(new CacmQueryParser(queriesFilename).parse());

		System.out.println("Searching cacm documents with cacm queries");
//		List<SearchResult> results = new QuerySearcher(documentList).search(queryList, RESULTS_LIMIT);
		List<SearchResult> results = new QuerySearcher(doclist, STOPWORDLIST).search(querylist, RESULTS_LIMIT);

		System.out.println("Printing results to output");
		printSearchResults(results);

		System.out.println("Writing trec-formated results to file");
		new TrecResults(results).write(TREC_SEARCHRESULTS_FILE);

		System.out.println("Parsing cacm qrels");
		List<String> cacmqrels = new CacmQrelsParser(CACM_QRELS_FILE).parse();

		System.out.println("Writing treq-formated qrels to file");
		new TrecQrels(cacmqrels).write(TREC_QRELS_FILE);

		System.out.println("Evaluating results with trec_eval");
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
