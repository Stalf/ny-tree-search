package com.holidu.interview.assignment.domain;

import java.util.Objects;

public class CountData {

    private long count;

    public CountData(long count) {
        this.count = count;
    }

    //for Jackson
    public CountData() {
    }

    public long getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountData countData = (CountData) o;
        return count == countData.count;
    }

    @Override
    public int hashCode() {
        return Objects.hash(count);
    }
}
