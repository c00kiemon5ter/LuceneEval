package rocchio;

import queryutils.QueryResults;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import queryutils.QueryUtils;

public class RocchioExpander implements QueryExpander {

	private final float alpha;
	private final float beta;
	private Analyzer analyzer;
	private final String field;

	public RocchioExpander(Analyzer analyzer, final String field) {
		this(analyzer, field, ALPHA, BETA);
	}

	public RocchioExpander(Analyzer analyzer, final String field,
			       final float alpha, final float beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.analyzer = analyzer;
		this.field = field;
	}

	@Override
	public Query expand(final QueryResults queryResults, final int docLimit, final int termLimit) throws ParseException {
		Query query = QueryUtils.normalizeQuery(queryResults.query().getQuery(), field, analyzer);
		/* TODO
		 * create index of relevant documents
		 * relevant documents is the first 'docLimit' documents of 'queryResults.queryResults()'
		 *
		 * sort terms in index according to tf-idf weight
		 * keep 'termsLimit' terms with highest weight
		 *
		 * add original query terms and new terms to form rocchio query
		 */
		return query;
	}
}
