package fr.esgi.tier_list.api.controller.rest;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.api.dto.CreateCompanyRequest;
import fr.esgi.tier_list.application.company.CompanyService;
import fr.esgi.tier_list.domain.exceptions.CompanyAlreadyExistsException;
import fr.esgi.tier_list.domain.exceptions.CompanyLimitReachedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyRestController {
    private final CompanyService companyService;

    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @Operation(
            summary = "Create a company",
            description = "Adds a new company logo. The number of companies is limited to 10 and the name must be unique."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Company successfully created"),
            @ApiResponse(responseCode = "409", description = "Company already exists", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "400", description = "Company limit reached", content = @Content(schema = @Schema())),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema()))
    })
    public ResponseEntity<Void> create(@RequestBody CreateCompanyRequest req) {
        try {
            companyService.addCompany(req.name(), req.logoUrl());
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (CompanyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        } catch (CompanyLimitReachedException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "List companies",
            description = "Returns the list of all available company logos"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of companies returned successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public List<CompanyResponse> list() {
        return companyService.listCompanies();
    }
}
