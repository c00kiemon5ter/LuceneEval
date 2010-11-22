package io;

import java.io.File;
import org.simpleframework.xml.core.Persister;

public class XmlReader<T> {

	private File xmlfile;
	private Class T;

	public XmlReader(String xmlfile, Class T) {
		this.xmlfile = new File(xmlfile);
		this.T = T;
	}

	@SuppressWarnings("unchecked")
	public T read() throws Exception {
		T data = (T) new Persister().read(T, xmlfile);
		return data;
	}
}
