package com.bpifrance.apibeneficiaires.interfacecontroller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpifrance.apibeneficiaires.application.usecase.GetBeneficiaries;
import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;
import com.bpifrance.apibeneficiaires.interfacecontroller.dto.BeneficiaryResponseDTO;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final GetBeneficiaries getUseCase;

    public CompanyController(GetBeneficiaries getUseCase) {
        this.getUseCase = getUseCase;
    }

    @GetMapping("/{id}/beneficiaries")
    public ResponseEntity<List<BeneficiaryResponseDTO>> getBeneficiaries(
            @PathVariable String id,
            @RequestParam(defaultValue = "all") String type) {

        Optional<List<Beneficiary>> result = getUseCase.execute(id, type);

        if (result.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        List<Beneficiary> list = result.get();

        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<BeneficiaryResponseDTO> dtos = list.stream()
                .map(b -> new BeneficiaryResponseDTO(
                        b.getId(),
                        b.getType().name(),
                        b.getPercentage().getValue()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
