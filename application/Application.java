package application;

import parsers.CacmDocParser;
import parsers.CacmQueryParser;

public class Application {
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Wrong argument count");
		}
		parseDocs(args[0]);
		parseQueries(args[1]);
	}

	private static void parseDocs(String docfile) {
		CacmDocParser docParser = new CacmDocParser(docfile);
		docParser.parse();
	}

	private static void parseQueries(String queriesfile) {
		CacmQueryParser queryParser = new CacmQueryParser(queriesfile);
		queryParser.parse();
	}
}
