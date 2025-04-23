package com.bpifrance.apibeneficiaires.application.usecase;

import com.bpifrance.apibeneficiaires.domain.model.Beneficiary;

import java.util.List;
import java.util.Optional;

public interface GetBeneficiaries {
    Optional<List<Beneficiary>> execute(String companyId, String type);
}
