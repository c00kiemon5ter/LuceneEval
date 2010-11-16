package parsers;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CacmQueryParser implements Parser {

	private File queriesfile;

	public CacmQueryParser(String queriesfile) {
		this.queriesfile = new File(queriesfile);
	}

	@Override
	public List parse() throws IOException {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
