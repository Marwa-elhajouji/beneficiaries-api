package com.bpifrance.apibeneficiaires.domain.model;

import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeneficiaryTest {

    @Test
    void should_hold_correct_data() {
        Beneficiary b = new Beneficiary("id123", Beneficiary.Type.COMPANY, new Percentage(10.5));

        assertThat(b.getId()).isEqualTo("id123");
        assertThat(b.getType()).isEqualTo(Beneficiary.Type.COMPANY);
        assertThat(b.getPercentage().getValue()).isEqualTo(10.5);
    }
}
