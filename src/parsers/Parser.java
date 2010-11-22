package parsers;

import java.io.IOException;
import java.util.Collection;

public interface Parser<T> {

	public Collection<T> parse() throws IOException;
}
