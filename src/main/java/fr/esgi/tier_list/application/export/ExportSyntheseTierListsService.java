package fr.esgi.tier_list.application.export;

import fr.esgi.tier_list.application.storage.StockageService;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.TierList;
import fr.esgi.tier_list.domain.port.ExportSyntheseTierListsUseCase;
import fr.esgi.tier_list.domain.port.LogoApiPort;
import fr.esgi.tier_list.domain.port.TierListDataSourcePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URI;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportSyntheseTierListsService implements ExportSyntheseTierListsUseCase {

    private final TierListDataSourcePort tierListDataSourcePort;
    private final LogoApiPort logoApiPort;
    private final StockageService stockageService;

    @Override
    public Map<String, CompanySynthesis> generateSynthesis() {
        List<TierList> allTierLists = tierListDataSourcePort.findAll();

        Map<String, CompanySynthesisBuilder> companiesCount = new HashMap<>();

        for (TierList tierList : allTierLists) {
            for (Tier tier : tierList.getList_tier()) {
                for (Company company : tier.getListCompany()) {
                    companiesCount
                            .computeIfAbsent(
                                    company.getName(),
                                    name -> new CompanySynthesisBuilder(name, company.getLogoUrl()))
                            .incrementForTier(tier.getName());
                }
            }
        }

        Map<String, CompanySynthesis> result = new LinkedHashMap<>();

        for (Map.Entry<String, CompanySynthesisBuilder> entry : companiesCount.entrySet()) {
            CompanySynthesisBuilder builder = entry.getValue();

            String logoStoragePath = fetchAndStoreCompanyLogo(builder.companyName, builder.logoUrl);

            result.put(
                    entry.getKey(),
                    new CompanySynthesis(
                            builder.companyName,
                            logoStoragePath,
                            builder.count,
                            builder.tiersCount));
        }

        return result.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue().count(), e1.getValue().count()))
                .collect(LinkedHashMap::new,
                        (map, entry) -> map.put(entry.getKey(), entry.getValue()),
                        LinkedHashMap::putAll);
    }
    
    private String fetchAndStoreCompanyLogo(String companyName, String logoUrl) {
        try {
            URI uri = new URI(logoUrl);
            String domain = uri.getHost();
            if (domain != null && domain.startsWith("www.")) {
                domain = domain.substring(4);
            }
            
            Optional<InputStream> logoStream = logoApiPort.fetchLogo(domain);
            
            if (logoStream.isPresent()) {
                String filename = "logos/" + companyName.replaceAll("[^a-zA-Z0-9]", "_") + ".png";
                stockageService.uploadImage(filename, logoStream.get());
                log.info("Logo stored for company: {} at {}", companyName, filename);
                return filename;
            } else {
                log.warn("Could not fetch logo for company: {}", companyName);
                return null;
            }
            
        } catch (Exception e) {
            log.error("Error processing logo for company: {}", companyName, e);
            return null;
        }
    }
    
    private static class CompanySynthesisBuilder {
        private final String companyName;
        private final String logoUrl;
        private int count = 0;
        private final Map<String, Integer> tiersCount = new HashMap<>();

        public CompanySynthesisBuilder(String companyName, String logoUrl) {
            this.companyName = companyName;
            this.logoUrl = logoUrl;
        }

        public void incrementForTier(String tierName) {
            count++;
            tiersCount.merge(tierName, 1, Integer::sum);
        }
    }
}
