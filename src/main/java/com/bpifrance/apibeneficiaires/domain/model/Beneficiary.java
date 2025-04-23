package com.bpifrance.apibeneficiaires.domain.model;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;

public class Beneficiary {   
    public enum Type { INDIVIDUAL, COMPANY }

    private final String id;
    private final Type type;
    private final Percentage percentage;

    public Beneficiary(String id, Type type, Percentage percentage) {
        this.id = id;
        this.type = type;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Percentage getPercentage() {
        return percentage;
    }
}
