package com.bpifrance.apibeneficiaires.config;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;
import com.bpifrance.apibeneficiaires.infrastructure.repository.InMemoryCompanyRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDataInitializer {

    private final InMemoryCompanyRepository companyRepository;

    public TestDataInitializer(InMemoryCompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void init() {
        Company company = new Company("123", "TestCorp");
        company.addBeneficiary(new Beneficiary("1", Beneficiary.Type.INDIVIDUAL, new Percentage(30)));
        companyRepository.save(company);


    }
}
