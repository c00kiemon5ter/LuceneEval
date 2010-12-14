package rocchio;

import cacm.CacmDocument;
import queryutils.QueryResults;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import queryutils.QueryUtils;

public class RocchioExpander implements QueryExpander {

	private float alpha;
	private float beta;
	private int limit;
	private QueryParser queryParser;

	public RocchioExpander(Analyzer analyzer, String field, int limit) {
		this(analyzer, field, ALPHA, BETA, limit);
	}

	public RocchioExpander(Analyzer analyzer, String field, float alpha, float beta, int limit) {
		this.alpha = alpha;
		this.beta = beta;
		this.limit = limit;
		this.queryParser = new QueryParser(Version.LUCENE_29, field, analyzer);
	}

	@Override
	public Query expand(QueryResults queryResults) throws ParseException {
		String querystr = QueryUtils.normalizeQuery(queryResults.query().getQuery());
		Query query = queryParser.parse(querystr);
		return query;
	}
}
