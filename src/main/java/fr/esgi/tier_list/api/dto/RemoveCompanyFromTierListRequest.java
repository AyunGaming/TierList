package fr.esgi.tier_list.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to remove a company from all tiers in a tier list")
public record RemoveCompanyFromTierListRequest(
                @Schema(description = "ID of the tier list") String tierListId,
                @Schema(description = "Name of the company to remove") String companyName) {
}
