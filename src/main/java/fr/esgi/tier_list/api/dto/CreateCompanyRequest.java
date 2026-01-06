package fr.esgi.tier_list.api.dto;

import fr.esgi.tier_list.infrastructure.company.CompanyJpaEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * DTO for {@link CompanyJpaEntity}
 */
public record CreateCompanyRequest(
        @Schema(description = "Company name", example = "Spotify")
        String name,

        @Schema(description = "URL of the company logo", example = "https://logo.dev/spotify.com")
        String logoUrl)
        implements Serializable {}