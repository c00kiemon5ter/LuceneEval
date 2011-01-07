package core;

import java.util.Comparator;
import java.util.Map.Entry;

public class ScoreComparator<T> implements Comparator<Entry<T, Float>> {

	@Override
	public int compare(Entry<T, Float> a, Entry<T, Float> b) {
		return a.getValue().compareTo(b.getValue());
	}
}
