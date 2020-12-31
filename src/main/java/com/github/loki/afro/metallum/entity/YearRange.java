package com.github.loki.afro.metallum.entity;

import java.util.Comparator;
import java.util.Objects;

public class YearRange implements Comparable<YearRange> {

    private final Year start;
    private final Year end;
    private final String asBandName;
    private final Long asBandId;

    private YearRange(Year start, Year end, String asBandName, Long asBandId) {
        this.start = start;
        this.end = end;
        this.asBandName = asBandName;
        this.asBandId = asBandId;
    }

    public static YearRange of(Year start, Year end, String asBandName, Long asBandId) {
        return new YearRange(start, end, asBandName, asBandId);
    }

    public String getAsBandName() {
        return asBandName;
    }

    public Long getAsBandId() {
        return asBandId;
    }

    public Year getStart() {
        return start;
    }

    public Year getEnd() {
        return end;
    }

    @Override
    public int compareTo(YearRange other) {
        return Comparator.comparing(YearRange::getEnd)
                .thenComparing(YearRange::getStart)
                .thenComparing(YearRange::getAsBandName)
                .compare(this, other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YearRange yearRange = (YearRange) o;
        return Objects.equals(start, yearRange.start)
                && Objects.equals(end, yearRange.end)
                && Objects.equals(asBandName, yearRange.asBandName)
                && Objects.equals(asBandId, yearRange.asBandId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, asBandName, asBandId);
    }

    @Override
    public String toString() {
        boolean startEndEquals = start.equals(end);

        String asBandIdSubString = "";
        if (asBandName != null) {
            String sub = asBandId != null ? "$" + asBandId : "";
            asBandIdSubString = " (as " + asBandName + sub + ")";
        }

        if (startEndEquals) {
            return start.getYear().toString() + asBandIdSubString;
        }
        return start.toString() + "-" + end.toString() + asBandIdSubString;
    }

    public static class Year implements Comparable<Year> {
        private final Integer year;
        private final boolean unknown;

        public Year(Integer year, boolean unknown) {
            this.year = year;
            this.unknown = unknown;
        }

        public Integer getYear() {
            return year;
        }

        public boolean isUnknown() {
            return unknown;
        }

        public static Year unknown() {
            return new Year(null, true);
        }

        public static Year present() {
            return new Year(null, false);
        }

        public static Year of(int year) {
            return new Year(year, false);
        }

        @Override
        public int compareTo(Year other) {
            Comparator<Integer> nullComparator = Comparator.nullsLast(Integer::compareTo);

            return Comparator.comparing(Year::getYear, nullComparator)
                    .compare(this, other);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Year year1 = (Year) o;
            return unknown == year1.unknown
                    && Objects.equals(year, year1.year);
        }

        @Override
        public int hashCode() {
            return Objects.hash(year, unknown);
        }

        @Override
        public String toString() {
            if (unknown) {
                return "?";
            } else if (year != null) {
                return year.toString();
            } else {
                return "present";
            }
        }
    }
}
