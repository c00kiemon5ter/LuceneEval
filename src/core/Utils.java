package core;

import cacm.CacmDocument;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

public class Utils {

	public static Query normalizeQuery(String query, String field, Analyzer analyzer) throws ParseException {
		QueryParser queryParser = new QueryParser(Version.LUCENE_29, field, analyzer);
		String pattern = "[\\\\|(|)|\"|?|*|;|\\-|\']";
		return queryParser.parse(query.replaceAll(pattern, " "));
	}

	public static Document convertToLDoc(CacmDocument cacmDocument) {
		Document document = new Document();
		document.add(new Field(CacmDocument.Fields.ABSTRACT,
				       cacmDocument.getAbstractInfo(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.AUTHORS,
				       cacmDocument.getAuthors().toString(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.DATE,
				       cacmDocument.getDate(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.ENTRYDATE,
				       cacmDocument.getEntrydate(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.ID,
				       String.valueOf(cacmDocument.getId()),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.REFERENCE,
				       cacmDocument.getReferences().toString(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.KEYWORDS,
				       cacmDocument.getKeywords().toString(),
				       Field.Store.YES, Field.Index.NO,
				       Field.TermVector.NO));
		document.add(new Field(CacmDocument.Fields.TITLE,
				       cacmDocument.getTitle(),
				       Field.Store.YES, Field.Index.ANALYZED,
				       Field.TermVector.YES));
		return document;
	}
}
