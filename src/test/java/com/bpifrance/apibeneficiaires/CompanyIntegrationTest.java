package com.bpifrance.apibeneficiaires;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Company;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;
import com.bpifrance.apibeneficiaires.infrastructure.repository.InMemoryCompanyRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryCompanyRepository companyRepository;

    @BeforeAll
    void setup() {
        Company company = new Company("123", "TestCorp");
        company.addBeneficiary(new Beneficiary("1", Beneficiary.Type.INDIVIDUAL, new Percentage(30)));
        companyRepository.save(company);
    }

    @Test
    void should_return_200_with_data() throws Exception {
        mockMvc.perform(get("/companies/123/beneficiaries?type=all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].percentage").exists());
    }

    @Test
    void should_return_404_if_company_not_found() throws Exception {
        mockMvc.perform(get("/companies/999/beneficiaries"))
                .andExpect(status().isNotFound());
    }
}
