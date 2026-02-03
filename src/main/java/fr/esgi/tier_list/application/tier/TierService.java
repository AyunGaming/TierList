package fr.esgi.tier_list.application.tier;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.exceptions.tier.TierNotFoundException;
import fr.esgi.tier_list.domain.port.CompanyDataSourcePort;
import fr.esgi.tier_list.domain.port.TierDataSourcePort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<fr.esgi.tier_list.domain.TierList> optionalTierList = tierListDataSourcePort.findById(tierListId);
        if (optionalTierList.isEmpty()) {
            return;
        }

        fr.esgi.tier_list.domain.TierList tierList = optionalTierList.get();

        Optional<Company> optionalCompany = companyDataSourcePort.findByName(companyName)
                .map(this::toDomain);
        if (optionalCompany.isEmpty()) {
            return;
        }

        Company company = optionalCompany.get();

        // Retirer la company de tous les tiers de cette tierlist
        for (Tier t : tierList.getList_tier()) {
            t.getListCompany().removeIf(c -> c.getName().equalsIgnoreCase(company.getName()));
        }

        // Trouver le tier cible
        Optional<Tier> optionalTier = tierList.getList_tier().stream()
                .filter(t -> t.getName().equalsIgnoreCase(tierName))
                .findFirst();
        if (optionalTier.isEmpty()) {
            return;
        }

        Tier targetTier = optionalTier.get();
        targetTier.getListCompany().add(company);

        tierListDataSourcePort.save(tierList);
    }

    public void removeCompanyFromTierList(String tierListId, String companyName) {
        Optional<fr.esgi.tier_list.domain.TierList> optionalTierList = tierListDataSourcePort.findById(tierListId);
        if (optionalTierList.isEmpty()) {
            return;
        }

        fr.esgi.tier_list.domain.TierList tierList = optionalTierList.get();

        // Retirer la company de tous les tiers de cette tierlist
        for (Tier t : tierList.getList_tier()) {
            t.getListCompany().removeIf(c -> c.getName().equalsIgnoreCase(companyName));
        }

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
