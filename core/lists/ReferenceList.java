package core.lists;

import core.Reference;
import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "references")
public class ReferenceList {

	@ElementList(inline = true)
	private List<Reference> references;

	public ReferenceList() {
		this.references = new LinkedList<Reference>();
	}

	public ReferenceList(ReferenceList references) {
		this.references = new LinkedList<Reference>(references.getReferences());
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		for (Reference reference : references) {
			strbuf.append("\n\t").append(reference.toString());
		}
		return strbuf.toString();
	}
}
