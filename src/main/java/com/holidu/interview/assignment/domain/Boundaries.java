package com.holidu.interview.assignment.domain;

import java.util.Objects;

public class Boundaries {

    private double x1;
    private double x2;
    private double y1;
    private double y2;

    public Boundaries(double x1, double x2, double y1, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boundaries that = (Boundaries) o;
        return Double.compare(that.x1, x1) == 0 &&
                Double.compare(that.x2, x2) == 0 &&
                Double.compare(that.y1, y1) == 0 &&
                Double.compare(that.y2, y2) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x1, x2, y1, y2);
    }

    public double getX1() {
        return x1;
    }

    public double getX2() {
        return x2;
    }

    public double getY1() {
        return y1;
    }

    public double getY2() {
        return y2;
    }
}
