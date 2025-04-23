package com.bpifrance.apibeneficiaires.domain.repository;

import com.bpifrance.apibeneficiaires.domain.model.Company;

import java.util.Optional;

public interface CompanyRepository {
    Optional<Company> findById(String id);
    void save(Company company);
}
