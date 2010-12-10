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
			String[] tokens = line.split("\\s+");
			int qid = Integer.parseInt(tokens[0]);
			int docId = Integer.parseInt(tokens[1]);
			int rel = tokens[2].equals(tokens[3]) ? 1 : 0;
			trecqrels.add(String.format("%d\tITER\t%d\t%d\n", qid, docId, rel));
		}
	}

	public void write(String trecQrelFilename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(trecQrelFilename)));
		for (String qrel : trecqrels) {
			writer.write(qrel);
		}
		writer.flush();
		writer.close();
	}
}
