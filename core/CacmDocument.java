package core;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "document")
public class CacmDocument {

	@ElementList(entry = "author")
	private List<Author> authors;
	@Element
	private String date;
	@Element(required = false)
	private String content;
	@Attribute
	private String id;
	@ElementList(entry = "keyword", required = false)
	private List<String> keywords;
	@Element
	private String entrydate;
	@Element(required = false)
	private String title;
	@Element(name = "abstract", required = false)
	private String abstractInfo;
	@ElementList(entry = "reference", required = false)
	private List<Reference> references;

	public static class Fields {

		public static final String AUTHORS = "Authors";
		public static final String DATE = "Date";
		public static final String ID = "Id";
		public static final String KEYWORDS = "Keywords";
		public static final String ENTRYDATE = "EntryDate";
		public static final String TITLE = "Title";
		public static final String ABSTRACT = "Abstract";
		public static final String REFERENCE = "Reference";
	}

	public CacmDocument() {
		this.authors = new LinkedList<Author>();
		this.references = new LinkedList<Reference>();
		this.keywords = new LinkedList<String>();
		this.abstractInfo =
		this.entrydate =
		this.id =
		this.content =
		this.title =
		this.date = "";
	}

	/**
	 * @return the authors
	 */
	public List<Author> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<Author> authors) {
		this.authors = new LinkedList<Author>(authors);
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the keywords
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(List<String> keywords) {
		this.keywords = new LinkedList<String>(keywords);
	}

	/**
	 * @return the entrydate
	 */
	public String getEntrydate() {
		return entrydate;
	}

	/**
	 * @param entrydate the entrydate to set
	 */
	public void setEntrydate(String entrydate) {
		this.entrydate = entrydate;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the abstractInfo
	 */
	public String getAbstractInfo() {
		return abstractInfo;
	}

	/**
	 * @param abstractInfo the abstractInfo to set
	 */
	public void setAbstractInfo(String abstractInfo) {
		this.abstractInfo = abstractInfo;
	}

	/**
	 * @return the references
	 */
	public List<Reference> getReferences() {
		return references;
	}

	/**
	 * @param references the reference to set
	 */
	public void setReferences(List<Reference> references) {
		this.references = new LinkedList<Reference>(references);
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		strbuf.append("DocID: ").append(id).
			append("\nTitle: ").append(title).
			append("\nAbstract: ").append(abstractInfo).
			append("\nDate: ").append(date).
			append("\nAuthors: ").append(authors).
			append("\nKeywords: ").append(keywords).
			append("\nContent: ").append(content).
			append("\nEntry Date: ").append(entrydate).
			append("\nReferences: ").append(references);
		return strbuf.toString();
	}

	public void addKeywords(String keyword) {
		this.keywords.add(keyword);
	}

	public void addAuthor(String author) {
		this.authors.add(new Author(author));
	}

	public void addDate(String date) {
		this.date += (this.date.isEmpty() ? "" : " ") + date;
	}

	public void addContent(String content) {
		this.content += (this.content.isEmpty() ? "" : " ") + content;
	}

	public void addEntrydate(String entrydate) {
		this.entrydate += (this.entrydate.isEmpty() ? "" : " ") + entrydate;
	}

	public void addTitle(String title) {
		this.title += (this.title.isEmpty() ? "" : " ") + title;
	}

	public void addAbstractInfo(String abstractTxt) {
		this.abstractInfo += (this.abstractInfo.isEmpty() ? "" : " ") + abstractTxt;
	}

	public void addReference(String col1, String col2, String col3) {
		this.references.add(new Reference(col1, col2, col3));
	}
}
