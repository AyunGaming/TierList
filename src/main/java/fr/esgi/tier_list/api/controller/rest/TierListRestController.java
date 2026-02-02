package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.api.dto.CreateTierListRequest;
import fr.esgi.tier_list.api.dto.TierListResponse;
import fr.esgi.tier_list.api.dto.TierResponse;
import fr.esgi.tier_list.application.tier_list.TierListService;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.TierList;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tier-lists")
@RequiredArgsConstructor
public class TierListRestController {

    private final TierListService tierListService;

    @Operation(summary = "Create a new tier list")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TierListResponse create(@RequestBody CreateTierListRequest request) {
        TierList tierList = tierListService.create(request.getPdfName(), request.getUserId());
        return toResponse(tierList);
    }

    @Operation(summary = "Get a tier list by id")
    @GetMapping("/{id}")
    public TierListResponse getById(@PathVariable String id) {
        return toResponse(tierListService.getById(id));
    }

    @Operation(summary = "Get all tier lists for a user")
    @GetMapping("/user/{userId}")
    public List<TierListResponse> getAllByUserId(@PathVariable String userId) {
        return tierListService.getAllByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Delete a tier list")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        tierListService.delete(id);
    }

    private TierListResponse toResponse(TierList tierList) {
        List<TierResponse> tierResponses = tierList.getList_tier().stream()
                .map(this::tierToResponse)
                .collect(Collectors.toList());

        return new TierListResponse(
                tierList.getId(),
                tierList.getPdf_name(),
                tierResponses,
                tierList.getUser_id());
    }

    private TierResponse tierToResponse(Tier tier) {
        return new TierResponse(
                tier.getId(),
                tier.getName(),
                tier.getListCompany().stream()
                        .map(c -> new CompanyResponse(null, c.getName(), c.getLogoUrl()))
                        .collect(Collectors.toList()),
                tier.getIndexPlacement());
    }
}
