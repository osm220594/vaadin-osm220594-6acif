package com.teppichcenter.domain;

import java.util.Objects;

public class Color {

    private String label;
    public Color(String label) {
        setLabel(label);

    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color color)) return false;
        return label == color.label;
    }
    @Override
    public int hashCode() {
        return Objects.hash(label);
    }
    @Override
    public String toString() {
        return "Color{" +
                "label='" + label +
                '}';
    }
}