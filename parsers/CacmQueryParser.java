package parsers;

import java.io.File;

public class CacmQueryParser implements Parser {

	private File queriesfile;

	public CacmQueryParser(String queriesfile) {
		this.queriesfile = new File(queriesfile);
	}

	@Override
	public void parse() {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
