package com.bpifrance.apibeneficiaires.domain.model;

import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompanyTest {

    @Test
    void should_add_a_beneficiary() {
        Company company = new Company("123", "MyCompany");

        Beneficiary b = new Beneficiary("b1", Beneficiary.Type.INDIVIDUAL, new Percentage(42));
        company.addBeneficiary(b);

        List<Beneficiary> result = company.getBeneficiaries();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("b1");
        assertThat(result.get(0).getPercentage().getValue()).isEqualTo(42.0);
    }

    @Test
    void should_return_id_and_name() {
        Company company = new Company("456", "CoolTech");
        assertThat(company.getId()).isEqualTo("456");
        assertThat(company.getName()).isEqualTo("CoolTech");
    }
}
