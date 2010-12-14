package rocchio;

import queryutils.QueryResults;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import queryutils.QueryUtils;

public class RocchioExpander implements QueryExpander {

	private float alpha;
	private float beta;
	private Analyzer analyzer;
	private String field;

	public RocchioExpander(Analyzer analyzer, String field) {
		this(analyzer, field, ALPHA, BETA);
	}

	public RocchioExpander(Analyzer analyzer, String field, float alpha, float beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.analyzer = analyzer;
		this.field = field;
	}

	@Override
	public Query expand(QueryResults queryResults, int docLimit) throws ParseException {
		Query query = QueryUtils.normalizeQuery(queryResults.query().getQuery(), field, analyzer);
		// TODO weights
		return query;
	}
}
