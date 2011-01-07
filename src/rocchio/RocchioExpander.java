package rocchio;

import core.Query;
import core.ScoreComparator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import java.util.AbstractMap.SimpleEntry;

public class RocchioExpander implements QueryExpander {

	private final float alpha;
	private final float beta;
	private final float gama;
	private final int docsLimit;
	private final int termsLimit;
	private Analyzer analyzer;
	private final String field;

	public RocchioExpander(Analyzer analyzer, final String field,
			       float alpha, float beta, float gama,
			       int docsLimit, int termsLimit) {
		this.analyzer = analyzer;
		this.field = field;
		this.alpha = alpha;
		this.beta = beta;
		this.gama = gama;
		this.docsLimit = docsLimit;
		this.termsLimit = termsLimit;
	}

	@Override
	public Query expand(final Query original, final List<Document> relevantDocs)
		throws ParseException, CorruptIndexException,
		       LockObtainFailedException, IOException {
		Directory index = createIndex(relevantDocs);
		// newQVector = alpha * oldQVector + beta * Sum{i=1..docs}( DocsVector )
		List<Entry<String, Float>> newQVector = getTermScoreList(index);
		for (String term : Arrays.asList(original.getQuery().toString(field).split("\\s+"))) {
			float score = alpha * Utils.getScore(index, term);
			boolean found = false;
			for (Entry<String, Float> entry : newQVector) {
				if (entry.getKey().equalsIgnoreCase(term)) {
					entry.setValue(entry.getValue() + score);
					found = true;
					break;
				}
			}
			if (!found) {
				newQVector.add(new SimpleEntry<String, Float>(term, score));
			}
		}
		Collections.sort(newQVector, new ScoreComparator<String>());
		Collections.reverse(newQVector);
		StringBuilder rocchioTerms = new StringBuilder();
		for (int limit = 0; limit < termsLimit && limit < newQVector.size(); limit++) {
			rocchioTerms.append(' ').append(newQVector.get(limit).getKey());
		}
		Query rocchioQuery = new Query(original.getQid(), Utils.normalizeQuery(rocchioTerms.toString(), field, analyzer));
		return rocchioQuery;
	}

	private Directory createIndex(List<Document> relevantDocs)
		throws CorruptIndexException, LockObtainFailedException, IOException {
		Directory index = new RAMDirectory();
		IndexWriter idxWriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
		for (int limit = 0; limit < docsLimit && limit < relevantDocs.size(); limit++) {
			idxWriter.addDocument(relevantDocs.get(limit));
		}
		idxWriter.optimize();
		idxWriter.close();
		return index;
	}

	private List<Entry<String, Float>> getTermScoreList(Directory index) throws CorruptIndexException, IOException {
		Map<String, Float> termScoreMap = new HashMap<String, Float>();
		IndexReader idxreader = IndexReader.open(index, true);
		TermEnum termEnum = idxreader.terms();
		TermDocs termDocs = idxreader.termDocs();
		int docsnum = idxreader.numDocs();
		while (termEnum.next()) {
			termDocs.seek(termEnum);
			if (termDocs.next()) {
				String term = termEnum.term().text();
				int tf = termDocs.freq();
				int df = termEnum.docFreq();
				float idf = Similarity.getDefault().idf(df, docsnum);
				float tfidf = tf * idf;
				termScoreMap.put(term, beta * tfidf);
			}
		}
		idxreader.close();
		return new ArrayList<Entry<String, Float>>(termScoreMap.entrySet());
	}
}
