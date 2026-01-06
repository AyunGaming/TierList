package fr.esgi.tier_list.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Company response")
public record CompanyResponse(

        @Schema(
                description = "Company identifier",
                example = "550e8400-e29b-41d4-a716-446655440000"
        )
        String id,

        @Schema(description = "Company name")
        String name,

        @Schema(description = "Company logo URL")
        String logoUrl
) {}