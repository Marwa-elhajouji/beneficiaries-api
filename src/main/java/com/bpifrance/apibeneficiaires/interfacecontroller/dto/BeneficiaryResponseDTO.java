package com.bpifrance.apibeneficiaires.interfacecontroller.dto;

public class BeneficiaryResponseDTO {

    private final String id;
    private final String type;
    private final double percentage;

    public BeneficiaryResponseDTO(String id, String type, double percentage) {
        this.id = id;
        this.type = type;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public double getPercentage() {
        return percentage;
    }
}
