package rocchio;

import java.io.IOException;
import java.util.Collection;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
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
	public Query expand(final Query original, final Collection<Document> relevantDocs,
			    final int docLimit, final int termLimit)
		throws ParseException, CorruptIndexException,
		       LockObtainFailedException, IOException {
		String queryTerms = original.toString(field);
		/* TODO
		 * sort terms in index according to tf-idf weight
		 * keep 'termsLimit' terms with highest weight
		 * add original query terms and new terms to form rocchio query
		 */
		Directory index = createIndex(relevantDocs);
		return QueryUtils.normalizeQuery(queryTerms, field, analyzer);
	}

	private Directory createIndex(Collection<Document> relevantDocs) throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory index = new RAMDirectory();
		IndexWriter idxWriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		for (Document document : relevantDocs) {
			idxWriter.addDocument(document);
		}
		idxWriter.close();
		return index;
	}
}
