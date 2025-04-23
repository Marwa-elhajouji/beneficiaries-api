package com.bpifrance.apibeneficiaires.application.usecase;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Beneficiary.Type;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.repository.CompanyRepository;
import com.bpifrance.apibeneficiaires.domain.service.BeneficiaryFilter;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;

class GetBeneficiariesUseCaseTest {

    private GetBeneficiariesUseCase useCase;

    private final Company root = new Company("123", "TestCorp");
    private final Company holding = new Company("child", "Holding");

    @BeforeEach
    void setup() {
        root.addBeneficiary(new Beneficiary("1", Type.INDIVIDUAL, new Percentage(30))); 
        root.addBeneficiary(new Beneficiary("child", Type.COMPANY, new Percentage(80))); 
        holding.addBeneficiary(new Beneficiary("3", Type.INDIVIDUAL, new Percentage(75)));
        root.addShareholder(holding);

        CompanyRepository fakeRepo = new CompanyRepository() {
            private final Map<String, Company> store = Map.of(
                "123", root,
                "child", holding
            );

            public Optional<Company> findById(String id) {
                return Optional.ofNullable(store.get(id));
            }

            public void save(Company company) {}
        };
        BeneficiaryFilter filter = new BeneficiaryFilter();
        useCase = new GetBeneficiariesUseCase(fakeRepo, filter);
    }

    @Test
    void should_return_individuals() {
        Optional<List<Beneficiary>> result = useCase.execute("123", "individuals");

        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(1);
        assertThat(result.get().get(0).getType()).isEqualTo(Type.INDIVIDUAL);
    }

    @Test
    void should_return_direct_effectives() {
        Optional<List<Beneficiary>> result = useCase.execute("123", "direct");

        assertThat(result).isPresent();
        assertThat(result.get().stream().map(Beneficiary::getId))
            .containsExactlyInAnyOrder("1");
    }

    @Test
    void should_return_indirect_effectives() {
        Optional<List<Beneficiary>> result = useCase.execute("123", "indirect");

        assertThat(result).isPresent();
        assertThat(result.get().stream()
            .filter(b -> b.getType() == Type.INDIVIDUAL)
            .map(Beneficiary::getId))
            .containsExactly("3");
    }

    @Test
    void should_return_combined_effectives() {
        Company company = new Company("456", "MixedCorp");
        company.addBeneficiary(new Beneficiary("yves", Type.INDIVIDUAL, new Percentage(10))); 
        company.addBeneficiary(new Beneficiary("sub", Type.COMPANY, new Percentage(80))); 

        Company sub = new Company("sub", "SubHolding");
        sub.addBeneficiary(new Beneficiary("yves", Type.INDIVIDUAL, new Percentage(60)));
        company.addShareholder(sub);

        CompanyRepository repo = new CompanyRepository() {
            public Optional<Company> findById(String id) {
                return Optional.of(company);
            }

            public void save(Company company) {}
        };

        GetBeneficiariesUseCase mixedUseCase = new GetBeneficiariesUseCase(repo, new BeneficiaryFilter());
        Optional<List<Beneficiary>> result = mixedUseCase.execute("456", "combined");

        assertThat(result).isPresent();
        assertThat(result.get().stream()
            .filter(b -> b.getType() == Type.INDIVIDUAL)
            .map(Beneficiary::getId))
            .containsExactly("yves");
        assertThat(result.get().get(0).getPercentage().getValue()).isEqualTo(58.0); 
    }

    @Test
    void should_return_empty_if_company_not_found() {
        Optional<List<Beneficiary>> result = useCase.execute("999", "combined");

        assertThat(result).isEmpty();
    }
}