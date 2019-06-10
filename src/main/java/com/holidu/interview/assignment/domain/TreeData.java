package com.holidu.interview.assignment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeData {

    private long id;
    private String name;
    private double x;
    private double y;

    public TreeData(long id, String name, double x, double y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    //for Jackson
    public TreeData() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeData treeData = (TreeData) o;
        return id == treeData.id &&
                Double.compare(treeData.x, x) == 0 &&
                Double.compare(treeData.y, y) == 0 &&
                Objects.equals(name, treeData.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, x, y);
    }
}
