package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.api.dto.AssignCompanyToTierRequest;
import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.api.dto.TierResponse;
import fr.esgi.tier_list.application.tier.TierService;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.exceptions.tier.TierNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiers")
public class TierRestController {

    private final TierService tierService;

    public TierRestController(TierService tierService) {
        this.tierService = tierService;
    }

    @Operation(summary = "List all tiers", description = "Returns the current tier list with assigned companies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tier list returned successfully")
    })
    @GetMapping
    public List<TierResponse> list() {
        return tierService.listTiers().stream()
                .map(this::toResponse)
                .toList();
    }

    @Operation(summary = "Assign a company to a tier", description = "Moves a company into the specified tier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Company assigned successfully"),
            @ApiResponse(responseCode = "404", description = "Tier or Company not found")
    })
    @PostMapping("/assign")
    public ResponseEntity<Void> assign(@RequestBody AssignCompanyToTierRequest request) {
        try {
            tierService.assignCompanyToTier(request.tierListId(), request.companyName(), request.tierName());
            return ResponseEntity.ok().build();
        } catch (TierNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private TierResponse toResponse(Tier tier) {
        return new TierResponse(
                tier.getId(),
                tier.getName(),
                tier.getListCompany().stream()
                        .map(c -> new CompanyResponse(null, c.getName(), c.getLogoUrl()))
                        .toList(),
                tier.getIndexPlacement());
    }
}
