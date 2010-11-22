package parsers;

import core.cacm.CacmQuery;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CacmQueryParser implements Parser<CacmQuery> {

	private BufferedReader reader;
	private List<CacmQuery> queries;

	private static class Fields {

		private static final char PREFIX = '.';
		private static final char AUTHORS = 'A';
		private static final char SOURCE = 'N';
		private static final char QUERY = 'W';
		private static final char ID = 'I';
	}

	public CacmQueryParser(String queriesfile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(new File(queriesfile)));
		this.queries = new LinkedList<CacmQuery>();
	}

	@Override
	public List<CacmQuery> parse() throws IOException {
		CacmQuery query = null;
		String line = "";
		char state = 0;
		while ((line = reader.readLine()) != null) {
			if ((line = line.trim()).isEmpty()) {
				continue;
			}
			if (line.charAt(0) == Fields.PREFIX) {
				state = line.charAt(1);
				if (state == Fields.ID) {
					if (query != null) {
						queries.add(query);
					}
					query = new CacmQuery();
					query.setId(line.substring(2).trim());
				}
			} else {
				switch (state) {
					case Fields.AUTHORS:
						query.addAuthor(line);
						break;
					case Fields.QUERY:
						query.addQuery(line);
						break;
					case Fields.SOURCE:
						query.addSource(line);
						break;
					/* Fields.ID and no state should never happen */
					case Fields.ID:
					default:
				}
			}
		}
		queries.add(query);
		reader.close();
		return queries;
	}
}
