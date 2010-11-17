package application;

import core.lists.CacmDocumentList;
import core.lists.CacmQueryList;
import io.XmlReader;
import io.XmlWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import parsers.CacmDocParser;
import parsers.CacmQueryParser;

public class Application {

	private CacmDocumentList doclist;
	private CacmQueryList querylist;

	public static void main(String[] args) {
		try {
			if (args.length != 2) {
				throw new IllegalArgumentException("Wrong argument count");
			}
			Application app = new Application(args[0], args[1]);
		} catch (Exception ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private Application(String collection, String queries) throws IOException, Exception {
		doclist = parseDocs(collection);
		querylist = parseQueries(queries);
//		writeCacmXmlDocument(doclist, collection.concat(".xml"));
//		doclist = readCacmXmlDocument(collection.concat(".xml"));
		evaluateQuerries(doclist, querylist);
	}

	private CacmDocumentList parseDocs(String docfile) throws IOException, Exception {
		CacmDocParser docParser = new CacmDocParser(docfile);
		return new CacmDocumentList(docParser.parse());
	}

	private CacmQueryList parseQueries(String queriesfile) throws IOException {
		CacmQueryParser queryParser = new CacmQueryParser(queriesfile);
		return new CacmQueryList(queryParser.parse());
	}

	private void writeCacmXmlDocument(CacmDocumentList cacmDocumentList, String xmlfile) throws Exception {
		new XmlWriter<CacmDocumentList>(xmlfile).write(cacmDocumentList);
	}

	private CacmDocumentList readCacmXmlDocument(String xmlfile) throws Exception {
		return new XmlReader<CacmDocumentList>(xmlfile, CacmDocumentList.class).read();
	}

	private void evaluateQuerries(CacmDocumentList doclist, CacmQueryList querylist) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
