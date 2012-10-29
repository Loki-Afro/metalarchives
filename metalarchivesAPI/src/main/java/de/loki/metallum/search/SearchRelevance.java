package de.loki.metallum.search;

public final class SearchRelevance implements Comparable<SearchRelevance> {

	private final Double	doubleIntern;

	public SearchRelevance(final Double doubleValue) {
		this.doubleIntern = doubleValue;
	}

	public SearchRelevance(final String doubleValue) {
		this.doubleIntern = Double.parseDouble(doubleValue);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.doubleIntern == null) ? 0 : this.doubleIntern.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SearchRelevance other = (SearchRelevance) obj;
		if (this.doubleIntern == null) {
			if (other.doubleIntern != null) {
				return false;
			}
		} else if (!this.doubleIntern.equals(other.doubleIntern)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SearchRelevance [doubleIntern=" + this.doubleIntern + "]";
	}

	@Override
	public int compareTo(SearchRelevance o) {
		// a negative integer, zero, or a positive integer as this object is less than, equal to, or
		// greater than the specified object.
		if (this.doubleIntern < o.doubleIntern) {
			return 1;
		} else if (this.doubleIntern > o.doubleIntern) {
			return -1;
		} else if (this.doubleIntern == o.doubleIntern) {
			return 0;
		}
		return 0;
	}

}
