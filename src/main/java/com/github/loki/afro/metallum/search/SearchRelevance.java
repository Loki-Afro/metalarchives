package com.github.loki.afro.metallum.search;

public final class SearchRelevance implements Comparable<SearchRelevance> {

    private final double doubleIntern;

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
        long temp;
        temp = Double.doubleToLongBits(this.doubleIntern);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
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
        return Double.doubleToLongBits(this.doubleIntern) == Double.doubleToLongBits(other.doubleIntern);
    }

    @Override
    public String toString() {
        return "SearchRelevance [doubleIntern=" + this.doubleIntern + "]";
    }

    @Override
    public int compareTo(final SearchRelevance o) {
        // a negative integer, zero, or a positive integer as this object is less than, equal to, or
        // greater than the specified object.
        if (this.doubleIntern < o.doubleIntern) {
            return 1;
        } else if (this.doubleIntern > o.doubleIntern) {
            return -1;
        } else if (Double.doubleToLongBits(this.doubleIntern) == Double.doubleToLongBits(o.doubleIntern)) {
            return 0;
        }
        return 0;
    }

}
