package trec;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrecQrels {

	private List<String> trecqrels;

	public TrecQrels(List<String> cacmQrels) {
		this.trecqrels = new ArrayList<String>(cacmQrels.size());
		for (String line : cacmQrels) {
			String[] tokens = line.split("\\s");
			trecqrels.add(String.format("%s %s %s", tokens[0], tokens[1],
						    (tokens[2].equals(tokens[3]) ? "1" : "0")));
		}
	}

	public void write(String trecQrelFilename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(trecQrelFilename)));
		for (String line : trecqrels) {
			writer.write(line);
		}
	}
}
