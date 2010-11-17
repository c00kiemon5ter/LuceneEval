package core;

import core.lists.AuthorList;

public class CacmQuery {

	private String id;
	private String source;
	private AuthorList authors;
	private String query;

	public CacmQuery() {
		this.authors = new AuthorList();
		this.query =
		this.id =
		this.source = "";
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
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the authors
	 */
	public AuthorList getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(AuthorList authors) {
		this.authors = authors;
	}

	public void addAuthor(String author) {
		this.authors.getAuthors().add(new Author(author));
	}

	public void addQuery(String query) {
		this.query += (this.query.isEmpty() ? "" : " ") + query;
	}

	public void addSource(String source) {
		this.source += (this.source.isEmpty() ? "" : " ") + source;
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		strbuf.append("QueryID: ").append(id).
			append("\nQuery: ").append(query).
			append("\nAuthors: ").append(authors).
			append("\nSource: ").append(source);
		return strbuf.toString();
	}
}
