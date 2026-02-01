package fr.esgi.tier_list.application.tier;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.exceptions.tier.TierNotFoundException;
import fr.esgi.tier_list.domain.port.CompanyDataSourcePort;
import fr.esgi.tier_list.domain.port.TierDataSourcePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TierService {

    private final TierDataSourcePort tierDataSourcePort;
    private final CompanyDataSourcePort companyDataSourcePort;

    public TierService(TierDataSourcePort tierDataSourcePort, CompanyDataSourcePort companyDataSourcePort) {
        this.tierDataSourcePort = tierDataSourcePort;
        this.companyDataSourcePort = companyDataSourcePort;
    }

    public List<Tier> listTiers() {
        return tierDataSourcePort.findAllOrderedByIndex();
    }

    public void assignCompanyToTier(String companyName, String tierName) {
        Tier tier = tierDataSourcePort.findByName(tierName)
                .orElseThrow(() -> new TierNotFoundException("Tier " + tierName + " not found"));

        Company company = companyDataSourcePort.findByName(companyName)
                .map(this::toDomain) // Map CompanyResponse to Company entity
                .orElseThrow(() -> new RuntimeException("Company " + companyName + " not found"));

        tier.getListCompany().add(company);
        tierDataSourcePort.save(tier);
    }

    private Company toDomain(CompanyResponse response) {
        return new Company(response.name(), response.logoUrl());
    }
}
