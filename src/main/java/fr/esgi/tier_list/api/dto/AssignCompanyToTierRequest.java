package fr.esgi.tier_list.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to assign a company to a tier")
public record AssignCompanyToTierRequest(
                @Schema(description = "ID of the target tier list") String tierListId,
                @Schema(description = "Name of the company to assign") String companyName,
                @Schema(description = "Name of the target tier") String tierName) {
}
