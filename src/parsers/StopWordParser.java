package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StopWordParser implements Parser<String> {

	private BufferedReader reader;
	private Set<String> stopwords;

	public StopWordParser(String stopwordfile) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(new File(stopwordfile)));
		stopwords = new HashSet<String>();
	}

	@Override
	public Set<String> parse() throws IOException {
		String line = "";
		while ((line = reader.readLine()) != null) {
			stopwords.add(line);
		}
		reader.close();
		return stopwords;
	}
}
