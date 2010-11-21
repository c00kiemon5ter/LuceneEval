package core.lists;

import core.cacm.CacmQuery;
import java.util.LinkedList;
import java.util.List;

public class CacmQueryList {

	private List<CacmQuery> queries;

	public CacmQueryList() {
		this.queries = new LinkedList<CacmQuery>();
	}

	public CacmQueryList(List<CacmQuery> queries) {
		this.queries = new LinkedList<CacmQuery>(queries);
	}

	/**
	 * @return the queries
	 */
	public List<CacmQuery> getQueries() {
		return queries;
	}

	/**
	 * @param queries the queries to set
	 */
	public void setQueries(List<CacmQuery> queries) {
		this.queries = queries;
	}

	@Override
	public String toString() {
		StringBuilder strbuf = new StringBuilder();
		for (CacmQuery query : queries) {
			strbuf.append(query.toString()).append("\n");
		}
		return strbuf.toString();
	}
}
