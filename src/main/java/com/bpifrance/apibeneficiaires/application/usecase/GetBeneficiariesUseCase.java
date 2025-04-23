package com.bpifrance.apibeneficiaires.application.usecase;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.repository.CompanyRepository;
import com.bpifrance.apibeneficiaires.domain.service.BeneficiaryFilter;

@Component
public class GetBeneficiariesUseCase implements GetBeneficiaries {

    private final CompanyRepository companyRepository;
    private final BeneficiaryFilter filter;

    public GetBeneficiariesUseCase(CompanyRepository companyRepository, BeneficiaryFilter filter) {
        this.companyRepository = companyRepository;
        this.filter = filter;
    }

    @Override
public Optional<List<Beneficiary>> execute(String companyId, String type) {
    return companyRepository.findById(companyId).map(company -> {
        return switch (type.toLowerCase()) {
            case "direct" -> filter.findDirectEffectives(company);
            case "indirect" -> filter.findIndirectEffectives(company);
            case "combined" -> filter.findCombinedEffectives(company);
            case "individuals" -> filter.findIndividuals(company.getBeneficiaries());
            default -> company.getBeneficiaries();
        };
    });
}
}
