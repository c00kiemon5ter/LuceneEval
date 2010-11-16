package io;

import java.io.File;
import org.simpleframework.xml.core.Persister;

public class XmlWriter<T> {
	private File xmlfile;

	public XmlWriter(String xmlfile) {
		this.xmlfile = new File(xmlfile);
	}

	public void write(T data) throws Exception {
		new Persister().write(data, xmlfile);
	}

}
