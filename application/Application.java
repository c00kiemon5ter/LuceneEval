package application;

import core.CacmDocument;
import core.CacmDocumentList;
import io.XmlReader;
import io.XmlWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import parsers.CacmDocParser;
import parsers.CacmQueryParser;

public class Application {

	public static void main(String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong argument count");
		}
		try {
			CacmDocumentList txtlist = parseDocs(args[0]);
			parseQueries(args[1]);
		} catch (Exception ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private static CacmDocumentList parseDocs(String docfile) throws IOException, Exception {
		CacmDocParser docParser = new CacmDocParser(docfile);
		List<CacmDocument> documents = docParser.parse();
		return new CacmDocumentList(documents);
	}

	private static void parseQueries(String queriesfile) throws IOException {
		CacmQueryParser queryParser = new CacmQueryParser(queriesfile);
		queryParser.parse();
	}

	private static void writeCacmXmlDocument(CacmDocumentList cacmDocumentList, String xmlfile) throws Exception {
		XmlWriter<CacmDocumentList> writer = new XmlWriter<CacmDocumentList>(xmlfile);
		writer.write(cacmDocumentList);
	}

	private static CacmDocumentList readCacmXmlDocument(String xmlfile) throws Exception {
		return new XmlReader<CacmDocumentList>(xmlfile, CacmDocumentList.class).read();
	}
}
