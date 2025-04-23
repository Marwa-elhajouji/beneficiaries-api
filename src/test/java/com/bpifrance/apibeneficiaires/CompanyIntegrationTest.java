package com.bpifrance.apibeneficiaires;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompanyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_direct_effectives() throws Exception {
        mockMvc.perform(get("/companies/123/beneficiaries?type=direct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].type").value("INDIVIDUAL"))
                .andExpect(jsonPath("$[0].percentage").value(30.0));
    }

    @Test
    void should_return_indirect_effectives() throws Exception {
        mockMvc.perform(get("/companies/456/beneficiaries?type=indirect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("yves"))
                .andExpect(jsonPath("$[0].type").value("INDIVIDUAL"))
                .andExpect(jsonPath("$[0].percentage").value(40.0));
    }

    @Test
    void should_return_combined_effectives() throws Exception {
        mockMvc.perform(get("/companies/789/beneficiaries?type=combined"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("yves"))
                .andExpect(jsonPath("$[0].percentage").value(64.0));
    }

    @Test
    void should_return_not_found_for_unknown_company() throws Exception {
        mockMvc.perform(get("/companies/999/beneficiaries?type=direct"))
                .andExpect(status().isNotFound());
    }
}
