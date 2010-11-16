package parsers;

import java.io.IOException;
import java.util.List;

public interface Parser<T> {

	public List<T> parse() throws IOException;
}
