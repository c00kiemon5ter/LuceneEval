package core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "author")
public class Author {

	@Element
	private String name;

	public Author() {
	}

	public Author(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("%s", name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
