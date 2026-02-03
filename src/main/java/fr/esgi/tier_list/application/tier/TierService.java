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
    private final fr.esgi.tier_list.domain.port.TierListDataSourcePort tierListDataSourcePort;

    public TierService(TierDataSourcePort tierDataSourcePort,
            CompanyDataSourcePort companyDataSourcePort,
            fr.esgi.tier_list.domain.port.TierListDataSourcePort tierListDataSourcePort) {
        this.tierDataSourcePort = tierDataSourcePort;
        this.companyDataSourcePort = companyDataSourcePort;
        this.tierListDataSourcePort = tierListDataSourcePort;
    }

    public List<Tier> listTiers() {
        return tierDataSourcePort.findAllOrderedByIndex();
    }

    public void assignCompanyToTier(String tierListId, String companyName, String tierName) {
        fr.esgi.tier_list.domain.TierList tierList = tierListDataSourcePort.findById(tierListId)
                .orElseThrow(() -> new RuntimeException("TierList not found"));

        Tier tier = tierList.getList_tier().stream()
                .filter(t -> t.getName().equalsIgnoreCase(tierName))
                .findFirst()
                .orElseThrow(() -> new TierNotFoundException("Tier " + tierName + " not found in this list"));

        Company company = companyDataSourcePort.findByName(companyName)
                .map(this::toDomain)
                .orElseThrow(() -> new RuntimeException("Company " + companyName + " not found"));

        tier.getListCompany().add(company);
        tierListDataSourcePort.save(tierList);
    }

    public void addTier(String name, int indexPlacement) {
        if (tierDataSourcePort.findByName(name).isPresent()) {
            return;
        }
        Tier tier = new Tier(java.util.UUID.randomUUID().toString(), name, indexPlacement);
        tierDataSourcePort.save(tier);
    }

    private Company toDomain(CompanyResponse response) {
        return new Company(response.name(), response.logoUrl());
    }
}
