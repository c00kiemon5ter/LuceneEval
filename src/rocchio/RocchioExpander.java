package rocchio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;
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
			    int docLimit, int termsLimit)
		throws ParseException, CorruptIndexException,
		       LockObtainFailedException, IOException {
		StringBuilder rocchioTerms = new StringBuilder(original.toString(field));
		Map<String, Float> termScoreMap = new HashMap<String, Float>(termsLimit);
		Directory index = createIndex(relevantDocs, docLimit);
		IndexReader idxreader = IndexReader.open(index, true);
		TermEnum termEnum = idxreader.terms();
		TermDocs termDocs = idxreader.termDocs();
		while (termEnum.next()) {
			termDocs.seek(termEnum);
			while (termDocs.next()) {
				int docsnum = idxreader.numDocs();
				int tf = termDocs.freq();
				int df = termEnum.docFreq();
				float idf = Similarity.getDefault().idf(df, docsnum);
				float tfidf = tf * idf;
				termScoreMap.put(termEnum.term().text(), tfidf);
			}
		}
		List<Entry<String, Float>> sortedTermScore = new ArrayList<Entry<String, Float>>(termScoreMap.entrySet());
		Collections.sort(sortedTermScore, new Comparator<Entry<String, Float>>() {

			@Override
			public int compare(Entry<String, Float> a, Entry<String, Float> b) {
				return a.getValue().compareTo(b.getValue());
			}
		});
		Iterator<Entry<String, Float>> iter = sortedTermScore.iterator();
		while (iter.hasNext() && --termsLimit >= 0) {
			rocchioTerms.append(' ').append(iter.next().getKey());
		}
		return QueryUtils.normalizeQuery(rocchioTerms.toString(), field, analyzer);
	}

	private Directory createIndex(Collection<Document> relevantDocs, int docLimit)
		throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory index = new RAMDirectory();
		IndexWriter idxWriter = new IndexWriter(index, analyzer, true,
							IndexWriter.MaxFieldLength.LIMITED);
		for (Iterator<Document> docIter = relevantDocs.iterator();
		     docIter.hasNext() && docLimit-- > 0;) {
			idxWriter.addDocument(docIter.next());
		}
		idxWriter.optimize();
		idxWriter.close();
		return index;
	}
}
