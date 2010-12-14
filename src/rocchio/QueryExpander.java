package rocchio;

import queryutils.QueryResults;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;

/**
 *
 * @author c00kiemon5ter
 */
public interface QueryExpander {

	public static final float ALPHA = 1.0F;
	public static final float BETA = 0.8F;
	public static final float GAMMA = 0.0F;

	Query expand(QueryResults queryResults) throws ParseException;
}
