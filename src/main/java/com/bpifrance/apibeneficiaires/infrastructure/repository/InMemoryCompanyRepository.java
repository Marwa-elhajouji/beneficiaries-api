package com.bpifrance.apibeneficiaires.infrastructure.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.repository.CompanyRepository;


@Repository
public class InMemoryCompanyRepository implements CompanyRepository {

    private final Map<String, Company> store = new HashMap<>();

    @Override
    public Optional<Company> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Company company) {
        store.put(company.getId(), company);
    }
}
