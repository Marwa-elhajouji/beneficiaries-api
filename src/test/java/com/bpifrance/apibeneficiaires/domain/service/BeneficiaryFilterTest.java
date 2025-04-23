package com.bpifrance.apibeneficiaires.domain.service;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Beneficiary.Type;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;

class BeneficiaryFilterTest {

    private BeneficiaryFilter filter;

    @BeforeEach
    void setUp() {
        filter = new BeneficiaryFilter();
    }

    @Test
    void should_return_only_individuals() {
        List<Beneficiary> list = List.of(
                new Beneficiary("1", Type.INDIVIDUAL, new Percentage(30)),
                new Beneficiary("2", Type.COMPANY, new Percentage(10))
        );

        List<Beneficiary> result = filter.findIndividuals(list);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(Type.INDIVIDUAL);
    }

    @Test
    void should_return_only_direct_effectives_above_25_percent() {
        Company company = new Company("1", "TestCo");
        company.addBeneficiary(new Beneficiary("A", Type.INDIVIDUAL, new Percentage(30)));
        company.addBeneficiary(new Beneficiary("B", Type.INDIVIDUAL, new Percentage(10)));

        List<Beneficiary> result = filter.findDirectEffectives(company);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("A");
    }

    @Test
    void should_return_only_indirect_effectives_above_25_percent() {
        Company root = new Company("Root", "RootCo");
        Company shareholder = new Company("Share", "ShareCo");

        shareholder.addBeneficiary(new Beneficiary("Indirect", Type.INDIVIDUAL, new Percentage(60)));
        root.addBeneficiary(new Beneficiary("Share", Type.COMPANY, new Percentage(50)));
        root.addShareholder(shareholder);

        List<Beneficiary> result = filter.findIndirectEffectives(root);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("Indirect");
        assertThat(result.get(0).getPercentage().getValue()).isEqualTo(30.0);
    }

    @Test
    void should_return_combined_effectives_above_25_percent() {
        Company root = new Company("Root", "RootCo");
        Company shareholder = new Company("Share", "ShareCo");

        shareholder.addBeneficiary(new Beneficiary("Yves", Type.INDIVIDUAL, new Percentage(60)));
        root.addBeneficiary(new Beneficiary("Yves", Type.INDIVIDUAL, new Percentage(10)));
        root.addBeneficiary(new Beneficiary("Share", Type.COMPANY, new Percentage(80)));
        root.addShareholder(shareholder);

        List<Beneficiary> result = filter.findCombinedEffectives(root);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("Yves");
        assertThat(result.get(0).getPercentage().getValue()).isEqualTo(58.0); 
    }
}
