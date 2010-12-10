package parsers;

import cacm.CacmDocument;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CacmDocParser implements Parser<CacmDocument> {

	private BufferedReader reader;
	private List<CacmDocument> documents;

	private static class Fields {

		private static final char PREFIX = '.';
		private static final char AUTHORS = 'A';
		private static final char DATE = 'B';
		private static final char CONTENT = 'C';
		private static final char ID = 'I';
		private static final char KEYWORDS = 'K';
		private static final char ENTRYDATE = 'N';
		private static final char TITLE = 'T';
		private static final char ABSTRACT = 'W';
		private static final char REFERENCE = 'X';
	}

	public CacmDocParser(String documentsFile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(new File(documentsFile)));
		this.documents = new LinkedList<CacmDocument>();
	}

	@Override
	public List<CacmDocument> parse() throws IOException {
		CacmDocument document = null;
		String line = "";
		char state = 0;
		while ((line = reader.readLine()) != null) {
			if ((line = line.trim()).isEmpty()) {
				continue;
			}
			if (line.charAt(0) == Fields.PREFIX) {
				state = line.charAt(1);
				if (state == Fields.ID) {
					if (document != null) {
						documents.add(document);
					}
					document = new CacmDocument();
					document.setId(line.substring(2).trim());
				}
			} else {
				switch (state) {
					case Fields.AUTHORS:
						document.addAuthor(line);
						break;
					case Fields.DATE:
						document.addDate(line);
						break;
					case Fields.CONTENT:
						document.addContent(line);
						break;
					case Fields.KEYWORDS:
						for (String keyword : line.split(",")) {
							document.addKeywords(keyword.trim());
						}
						break;
					case Fields.ENTRYDATE:
						document.addEntrydate(line);
						break;
					case Fields.TITLE:
						document.addTitle(line);
						break;
					case Fields.ABSTRACT:
						document.addAbstractInfo(line);
						break;
					case Fields.REFERENCE:
						String[] cols = line.split("\t");
						document.addReference(cols[0], cols[1], cols[2]);
						break;
					/* Fields.ID and no state should never happen */
					case Fields.ID:
					default:
				}
			}
		}
		documents.add(document);
		reader.close();
		return documents;
	}
}
