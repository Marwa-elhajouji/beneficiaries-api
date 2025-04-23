package com.bpifrance.apibeneficiaires.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PercentageTest {

    @Test
    void should_return_true_if_value_greater_than_threshold() {
        Percentage p = new Percentage(30.0);
        assertThat(p.isGreaterThan(25.0)).isTrue();
    }

    @Test
    void should_return_false_if_value_equals_threshold() {
        Percentage p = new Percentage(25.0);
        assertThat(p.isGreaterThan(25.0)).isFalse();
    }
}
