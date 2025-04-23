package com.bpifrance.apibeneficiaires.infrastructure.repository;

import com.bpifrance.apibeneficiaires.domain.model.Company;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryCompanyRepositoryTest {

    @Test
    void findById_should_return_company_if_exists() {
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        Company c = new Company("123", "TestCorp");
        repo.save(c);

        Optional<Company> result = repo.findById("123");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("TestCorp");
    }

    @Test
    void findById_should_return_empty_if_not_found() {
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        Optional<Company> result = repo.findById("not-found");

        assertThat(result).isEmpty();
    }

    @Test
    void save_should_override_if_same_id() {
        InMemoryCompanyRepository repo = new InMemoryCompanyRepository();
        Company oldOne = new Company("123", "OldCorp");
        Company newOne = new Company("123", "NewCorp");

        repo.save(oldOne);
        repo.save(newOne);

        Optional<Company> result = repo.findById("123");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("NewCorp");
    }
}
