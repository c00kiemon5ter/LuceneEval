package core;

import java.util.LinkedList;
import java.util.List;

public class CacmDocument {

	private List<String> authors;
	private String date;
	private String content;
	private String id;
	private String keywords;
	private String entrydate;
	private String title;
	private String abstractInfo;
	private List<String> references;

	public CacmDocument() {
		this.authors = new LinkedList<String>();
		this.references = new LinkedList<String>();
		this.abstractInfo =
		this.entrydate =
		this.keywords =
		this.id =
		this.content =
		this.title =
		this.date = "";
	}

	/**
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = new LinkedList<String>(authors);
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
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
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
	public List<String> getReferences() {
		return references;
	}

	/**
	 * @param references the reference to set
	 */
	public void setReferences(List<String> references) {
		this.references = new LinkedList<String>(references);
	}

	@Override
	public String toString() {
		return String.format("DocID: %s\nTitle: %s\nAbstract: %s\n"
				     + "Date: %s\nAuthors: %s\nKeywords: %s\n"
				     + "Content: %s\nEntry Date: %s\nReference: %s",
				     id, title, abstractInfo, date, authors,
				     keywords, content, entrydate, references);
	}

	public void addKeywords(String keywords) {
		this.keywords.concat(keywords);
	}

	public void addAuthor(String author) {
		this.authors.add(author);
	}

	public void addDate(String date) {
		this.date.concat(date);
	}

	public void addContent(String content) {
		this.content.concat(content);
	}

	public void addEntrydate(String entrydate) {
		this.entrydate.concat(entrydate);
	}

	public void addTitle(String title) {
		this.title.concat(title);
	}

	public void addAbstractInfo(String abstractTxt) {
		this.abstractInfo.concat(abstractTxt);
	}

	public void addReference(String reference) {
		this.references.add(reference);
	}
}
