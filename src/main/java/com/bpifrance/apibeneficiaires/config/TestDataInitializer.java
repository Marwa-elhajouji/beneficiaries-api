package com.bpifrance.apibeneficiaires.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;
import com.bpifrance.apibeneficiaires.infrastructure.repository.InMemoryCompanyRepository;

import jakarta.annotation.PostConstruct;

@Configuration
public class TestDataInitializer {

    private static final Logger logger = LoggerFactory.getLogger(TestDataInitializer.class);

    private final InMemoryCompanyRepository companyRepository;

    public TestDataInitializer(InMemoryCompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @PostConstruct
    public void init() {

        Company directCompany = new Company("123", "TestCorp");
        directCompany.addBeneficiary(new Beneficiary("1", Beneficiary.Type.INDIVIDUAL, new Percentage(30)));
        companyRepository.save(directCompany);
        logger.info("✅ Initialized direct company: 123");

        Company indirectRoot = new Company("456", "IndirectRoot");
        Company holding = new Company("H1", "HoldingCompany");

        indirectRoot.addBeneficiary(new Beneficiary("H1", Beneficiary.Type.COMPANY, new Percentage(80)));
        indirectRoot.addShareholder(holding);
        holding.addBeneficiary(new Beneficiary("yves", Beneficiary.Type.INDIVIDUAL, new Percentage(50)));

        companyRepository.save(indirectRoot);
        companyRepository.save(holding);
        logger.info("✅ Initialized indirect company: 456");

        Company combinedCompany = new Company("789", "CombinedCorp");
        Company sub = new Company("SUB", "SubHolding");

        combinedCompany.addBeneficiary(new Beneficiary("yves", Beneficiary.Type.INDIVIDUAL, new Percentage(10))); 
        combinedCompany.addBeneficiary(new Beneficiary("SUB", Beneficiary.Type.COMPANY, new Percentage(60)));
        combinedCompany.addShareholder(sub);
        sub.addBeneficiary(new Beneficiary("yves", Beneficiary.Type.INDIVIDUAL, new Percentage(90)));

        companyRepository.save(combinedCompany);
        companyRepository.save(sub);
        logger.info("✅ Initialized combined company: 789");

        logger.info("🎯 Test data initialization completed successfully.");
    }
}
