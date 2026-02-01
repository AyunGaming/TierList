package fr.esgi.tier_list.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Tier response containing its name, companies and position")
public record TierResponse(
        @Schema(description = "Tier ID") String id,

        @Schema(description = "Tier name") String name,

        @Schema(description = "List of companies in the tier") List<CompanyResponse> listCompany,

        @Schema(description = "Tier placement index") int indexPlacement) {
}
