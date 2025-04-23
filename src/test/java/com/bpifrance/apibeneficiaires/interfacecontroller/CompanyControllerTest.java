package com.bpifrance.apibeneficiaires.interfacecontroller;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bpifrance.apibeneficiaires.application.usecase.GetBeneficiariesUseCase;
import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.domain.model.Beneficiary.Type;
import com.bpifrance.apibeneficiaires.domain.valueobject.Percentage;

@WebMvcTest(controllers = CompanyController.class)
class CompanyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetBeneficiariesUseCase useCase;

    @Test
    void should_return_200_with_one_effective_beneficiary() throws Exception {
        Beneficiary b = new Beneficiary("1", Type.INDIVIDUAL, new Percentage(30));

        when(useCase.execute("123", "effective")).thenReturn(Optional.of(List.of(b)));

        mockMvc.perform(get("/companies/123/beneficiaries?type=effective"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].percentage").value(30.0));
    }

    @Test
    void should_return_204_when_beneficiaries_list_is_empty() throws Exception {
        when(useCase.execute("123", "effective")).thenReturn(Optional.of(List.of()));

        mockMvc.perform(get("/companies/123/beneficiaries?type=effective"))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_404_when_company_not_found() throws Exception {
        when(useCase.execute("999", "effective")).thenReturn(Optional.empty());

        mockMvc.perform(get("/companies/999/beneficiaries?type=effective"))
                .andExpect(status().isNotFound());
    }
}
