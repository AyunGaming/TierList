package fr.esgi.tier_list.domain.port;

import java.util.Map;

public interface ExportSyntheseTierListsUseCase {
    Map<String, CompanySynthesis> generateSynthesis();

    record CompanySynthesis(String companyName,
                            String logoUrl,
                            int count,
                            Map<String, Integer> tiersCount) {
    }
}
