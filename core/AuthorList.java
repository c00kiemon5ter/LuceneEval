package core;

import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "authors")
public class AuthorList {

	@ElementList(inline = true, required=false)
	private List<Author> authors;

	public AuthorList() {
		authors = new LinkedList<Author>();
	}

	public AuthorList(AuthorList authors) {
		this.authors = new LinkedList<Author>(authors.getAuthors());
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		for (Author author : authors) {
			strbuf.append("\n\t").append(author.toString());
		}
		return strbuf.toString();
	}
}
