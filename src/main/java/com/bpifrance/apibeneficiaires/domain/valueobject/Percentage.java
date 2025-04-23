package com.bpifrance.apibeneficiaires.domain.valueobject;

public class Percentage {

    private final double value;

    public Percentage(double value) {
        if (value < 0 || value > 100) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean isGreaterThan(double threshold) {
        return value > threshold;
    }
}
