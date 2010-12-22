package rocchio;

import java.io.IOException;
import java.util.Collection;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.LockObtainFailedException;

/**
 *
 * @author c00kiemon5ter
 */
public interface QueryExpander {

	public static final float ALPHA = 1.0F;
	public static final float BETA = 0.75F;
	public static final float GAMMA = 0.0F;

	Query expand(final Query original, final Collection<Document> relevantDocs,
		     final int docLimit, final int extraTermsLimit)
		throws ParseException, CorruptIndexException,
		       LockObtainFailedException, IOException;
}
