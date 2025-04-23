package com.bpifrance.apibeneficiaires.domain.model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Company {
    private final String id;
    private final String name;
    private final List<Beneficiary> beneficiaries;
    private final List<Company> shareholders;

    public Company(String id, String name) {
        this.id = id;
        this.name = name;
        this.beneficiaries = new ArrayList<>();
        this.shareholders = new ArrayList<>();
    }

    public void addBeneficiary(Beneficiary beneficiary) {
        this.beneficiaries.add(beneficiary);
    }

    public void addShareholder(Company company) {
        this.shareholders.add(company);
    }

    public List<Beneficiary> getBeneficiaries() {
        return Collections.unmodifiableList(beneficiaries);
    }

    public List<Company> getShareholders() {
        return Collections.unmodifiableList(shareholders);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}