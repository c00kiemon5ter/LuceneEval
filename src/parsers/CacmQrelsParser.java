package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class CacmQrelsParser implements Parser<String> {

	private File qrelsfile;
	private List<String> qrels;

	public CacmQrelsParser(String qrelsFilename) {
		this.qrelsfile = new File(qrelsFilename);
		this.qrels = new LinkedList<String>();
	}

	@Override
	public List<String> parse() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(qrelsfile));
		String line;
		while ((line = reader.readLine()) != null) {
			qrels.add(line);
		}
		reader.close();
		return qrels;
	}
}
