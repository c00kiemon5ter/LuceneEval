package cacm.lists;

import cacm.CacmDocument;
import java.util.LinkedList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "documents")
public class CacmDocumentList {

	@ElementList(inline = true)
	private List<CacmDocument> documents;

	public CacmDocumentList() {
		this.documents = new LinkedList<CacmDocument>();
	}

	public CacmDocumentList(List<CacmDocument> documents) {
		this.documents = new LinkedList<CacmDocument>(documents);
	}

	public List<CacmDocument> getDocuments() {
		return documents;
	}

	public void setDocuments(List<CacmDocument> documents) {
		this.documents = documents;
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		for (CacmDocument doc : documents) {
			strbuf.append(doc.toString()).append("\n");
		}
		return strbuf.toString();
	}
}
