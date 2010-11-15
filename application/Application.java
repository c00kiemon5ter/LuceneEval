package application;

import java.io.IOException;
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
			parseDocs(args[0]);
//			parseQueries(args[1]);
		} catch (IOException ex) {
			Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void parseDocs(String docfile) throws IOException {
		CacmDocParser docParser = new CacmDocParser(docfile);
		docParser.parse();
	}

	private static void parseQueries(String queriesfile) {
		CacmQueryParser queryParser = new CacmQueryParser(queriesfile);
		queryParser.parse();
	}
}
