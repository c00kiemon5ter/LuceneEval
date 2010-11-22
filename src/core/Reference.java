package core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class Reference {

	@Element
	private String col1;
	@Element
	private String col2;
	@Element
	private String col3;

	public Reference() {
	}

	public Reference(String col1,
			 String col2,
			 String col3) {
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
	}

	@Override
	public String toString() {
		return String.format("%s\t%s\t%s", col1, col2, col3);
	}

	/**
	 * @return the col1
	 */
	public String getCol1() {
		return col1;
	}

	/**
	 * @param col1 the col1 to set
	 */
	public void setCol1(String col1) {
		this.col1 = col1;
	}

	/**
	 * @return the col2
	 */
	public String getCol2() {
		return col2;
	}

	/**
	 * @param col2 the col2 to set
	 */
	public void setCol2(String col2) {
		this.col2 = col2;
	}

	/**
	 * @return the col3
	 */
	public String getCol3() {
		return col3;
	}

	/**
	 * @param col3 the col3 to set
	 */
	public void setCol3(String col3) {
		this.col3 = col3;
	}
}
