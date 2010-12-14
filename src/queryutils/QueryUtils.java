package queryutils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class QueryUtils {

	public static Query normalizeQuery(String query, String field, Analyzer analyzer) throws ParseException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_29, field, analyzer);
		String pattern = "[\\\\|(|)|\"|?|*|;|\\-|\']";
		return queryParser.parse(query.replaceAll(pattern, " "));
	}
}
