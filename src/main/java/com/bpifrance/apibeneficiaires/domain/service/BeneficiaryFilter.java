package com.bpifrance.apibeneficiaires.domain.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;

@Service
public class BeneficiaryFilter {

    public List<Beneficiary> findDirectEffectives(Company company) {
        return company.getBeneficiaries().stream()
                .filter(b -> b.getType() == Beneficiary.Type.INDIVIDUAL)
                .filter(b -> b.getPercentage().getValue() > 25.0)
                .toList();
    }

    public List<Beneficiary> findIndirectEffectives(Company root) {
        Map<String, Double> indirectHoldings = new HashMap<>();
        accumulateHoldings(root, 1.0, indirectHoldings, true);
        return indirectHoldings.entrySet().stream()
                .filter(entry -> entry.getValue() * 100 > 25.0)
                .filter(entry -> isIndividual(entry.getKey(), root))
                .map(entry -> new Beneficiary(entry.getKey(), Beneficiary.Type.INDIVIDUAL,
                        new Percentage(round(entry.getValue() * 100))))
                .toList();
    }

    public List<Beneficiary> findCombinedEffectives(Company root) {
        Map<String, Double> totalHoldings = new HashMap<>();
        accumulateHoldings(root, 1.0, totalHoldings, false);
        return totalHoldings.entrySet().stream()
                .filter(entry -> entry.getValue() * 100 > 25.0)
                .filter(entry -> isIndividual(entry.getKey(), root))
                .map(entry -> new Beneficiary(entry.getKey(), Beneficiary.Type.INDIVIDUAL,
                        new Percentage(round(entry.getValue() * 100))))
                .toList();
    }

    private void accumulateHoldings(Company company, double weight, Map<String, Double> accumulator, boolean indirectOnly) {
        for (Beneficiary b : company.getBeneficiaries()) {
    boolean isDirectPhysicalPerson = b.getType() == Beneficiary.Type.INDIVIDUAL && weight == 1.0;
    if (!indirectOnly || !isDirectPhysicalPerson) {
        accumulator.merge(b.getId(), weight * b.getPercentage().getValue() / 100.0, Double::sum);
    }
}


        for (Company shareholder : company.getShareholders()) {
            double shareholderPercentage = company.getBeneficiaries().stream()
                    .filter(b -> b.getId().equals(shareholder.getId()))
                    .mapToDouble(b -> b.getPercentage().getValue())
                    .findFirst().orElse(0.0);

            accumulateHoldings(shareholder, weight * shareholderPercentage / 100.0, accumulator, indirectOnly);
        }
    }

    private boolean isIndividual(String beneficiaryId, Company company) {
        if (company.getBeneficiaries().stream()
                .anyMatch(b -> b.getId().equals(beneficiaryId) && b.getType() == Beneficiary.Type.INDIVIDUAL)) {
            return true;
        }

        for (Company shareholder : company.getShareholders()) {
            if (isIndividual(beneficiaryId, shareholder)) {
                return true;
            }
        }
        return false;
    }

    public List<Beneficiary> findIndividuals(List<Beneficiary> beneficiaries) {
        return beneficiaries.stream()
                .filter(b -> b.getType() == Beneficiary.Type.INDIVIDUAL)
                .toList();
    }

    private double round(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
