package trec;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrecProcess {

	public static final String TREC_EXECUTABLE = "trec_eval";
	private String trecQrelsFile;
	private String trecSearchResultsFile;
	private FileOutputStream fos;

	public TrecProcess(String trecQrelsFile, String trecSearchResultsFile, String trecResultsFile) throws FileNotFoundException {
		this.trecQrelsFile = new File(trecQrelsFile).getAbsolutePath();
		this.trecSearchResultsFile = new File(trecSearchResultsFile).getAbsolutePath();
		fos = new FileOutputStream(new File(trecResultsFile));
	}

	public int run() throws IOException, InterruptedException {
		Process proc = Runtime.getRuntime().exec(new String[]{TREC_EXECUTABLE, trecQrelsFile, trecSearchResultsFile});
		StreamHandler stdout = new StreamHandler(proc.getInputStream(), fos);
		StreamHandler stderr = new StreamHandler(proc.getErrorStream(), System.err);
		stdout.start();
		stderr.start();
		stdout.join();
		stderr.join();
		int exit_code = proc.waitFor();
		fos.flush();
		fos.close();
		return exit_code;
	}

	private class StreamHandler extends Thread {

		InputStream is;
		OutputStream os;

		StreamHandler(InputStream input, OutputStream output) {
			this.is = input;
			this.os = output;
		}

		@Override
		public void run() {
			try {
				PrintWriter writer = new PrintWriter(os);
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				String line;
				while ((line = reader.readLine()) != null) {
					writer.println(line);
				}
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				Logger.getLogger(TrecProcess.StreamHandler.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
