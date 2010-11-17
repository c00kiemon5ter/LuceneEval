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

	public static void main(String[] args) {
		try {
			if (args.length != 2) {
				throw new IllegalArgumentException("Wrong argument count");
			}
			CacmDocumentList doclist = parseDocs(args[0]);
			CacmQueryList querylist = parseQueries(args[1]);
		} catch (Exception ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, "Fatal Error: ", ex);
			System.exit(1);
		}
	}

	private static CacmDocumentList parseDocs(String docfile) throws IOException, Exception {
		CacmDocParser docParser = new CacmDocParser(docfile);
		return new CacmDocumentList(docParser.parse());
	}

	private static CacmQueryList parseQueries(String queriesfile) throws IOException {
		CacmQueryParser queryParser = new CacmQueryParser(queriesfile);
		return new CacmQueryList(queryParser.parse());
	}

	private static void writeCacmXmlDocument(CacmDocumentList cacmDocumentList, String xmlfile) throws Exception {
		XmlWriter<CacmDocumentList> writer = new XmlWriter<CacmDocumentList>(xmlfile);
		writer.write(cacmDocumentList);
	}

	private static CacmDocumentList readCacmXmlDocument(String xmlfile) throws Exception {
		return new XmlReader<CacmDocumentList>(xmlfile, CacmDocumentList.class).read();
	}
}
