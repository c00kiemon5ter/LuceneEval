package rocchio;

import core.Query;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.lucene.search.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import core.Utils;

public class RocchioExpander implements QueryExpander {

	private final float alpha;
	private final float beta;
	private final int docsLimit;
	private final int termsLimit;
	private Analyzer analyzer;
	private final String field;

	public RocchioExpander(Analyzer analyzer, final String field,
			       int docsLimit, int extraTermsLimit) {
		this(analyzer, field, docsLimit, extraTermsLimit, ALPHA, BETA);
	}

	public RocchioExpander(Analyzer analyzer, final String field,
			       int docsLimit, int extraTermsLimit,
			       float alpha, float beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.docsLimit = docsLimit;
		this.termsLimit = extraTermsLimit;
		this.analyzer = analyzer;
		this.field = field;
	}

	@Override
	public Query expand(final Query original, final List<Document> relevantDocs)
		throws ParseException, CorruptIndexException,
		       LockObtainFailedException, IOException {
		Directory index = createIndex(relevantDocs, docsLimit);
		Map<String, Float> termScoreMap = getTermScoreMap(index);
		List<Entry<String, Float>> sortedTermScoreList = getSortedTermScores(termScoreMap);
		Set<String> terms = new HashSet<String>(Arrays.asList(original.getQuery().toString(field).split("\\s+")));
		Iterator<Entry<String, Float>> iter = sortedTermScoreList.iterator();
		int limit = termsLimit;
		while (iter.hasNext() && limit > 0) {
			if (terms.add(iter.next().getKey())) {
				--limit;
			}
		}
		StringBuilder rocchioTerms = new StringBuilder(terms.size());
		for (String term : terms) {
			rocchioTerms.append(' ').append(term);
		}
		return new Query(original.getQid(), Utils.normalizeQuery(
			rocchioTerms.toString(), field, analyzer));
	}

	private Directory createIndex(List<Document> relevantDocs, int docLimit)
		throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory index = new RAMDirectory();
		IndexWriter idxWriter = new IndexWriter(index, analyzer, true,
							IndexWriter.MaxFieldLength.LIMITED);
		Iterator<Document> docIter = relevantDocs.iterator();
		while (docIter.hasNext() && docLimit-- > 0) {
			idxWriter.addDocument(docIter.next());
		}
		idxWriter.optimize();
		idxWriter.close();
		return index;
	}

	private Map<String, Float> getTermScoreMap(Directory index) throws CorruptIndexException, IOException {
		Map<String, Float> termScoreMap = new HashMap<String, Float>();
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
		return termScoreMap;
	}

	private List<Entry<String, Float>> getSortedTermScores(Map<String, Float> termScoreMap) {
		List<Entry<String, Float>> sortedTermScoreList = new ArrayList<Entry<String, Float>>(termScoreMap.entrySet());
		Collections.sort(sortedTermScoreList, new Comparator<Entry<String, Float>>() {

			@Override
			public int compare(Entry<String, Float> a, Entry<String, Float> b) {
				return a.getValue().compareTo(b.getValue());
			}
		});
		return sortedTermScoreList;
	}
}
