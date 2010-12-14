package queryutils;

public class QueryUtils {

	public static String normalizeQuery(String query) {
		String pattern = "[\\\\|(|)|\"|?|*|;|\\-|\']";
		return query.replaceAll(pattern, " ");
	}
}
