package fr.esgi.tier_list.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.esgi.tier_list.api.dto.CreateCompanyRequest;
import fr.esgi.tier_list.application.company.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class CompanyDataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CompanyDataLoader.class);
    private final CompanyService companyService;
    private final ObjectMapper objectMapper;

    public CompanyDataLoader(CompanyService companyService, ObjectMapper objectMapper) {
        this.companyService = companyService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) {
        try {
            ClassPathResource resource = new ClassPathResource("data/companies.json");
            if (!resource.exists()) {
                logger.info("No companies.json file found, skipping data load");
                return;
            }

            try (InputStream inputStream = resource.getInputStream()) {
                List<CreateCompanyRequest> companies = objectMapper.readValue(
                        inputStream,
                        new TypeReference<List<CreateCompanyRequest>>() {}
                );

                logger.info("Loading {} companies from companies.json", companies.size());

                for (CreateCompanyRequest company : companies) {
                    try {
                        companyService.addCompany(company.name(), company.logoUrl());
                        logger.info("Added company: {}", company.name());
                    } catch (Exception e) {
                        logger.warn("Failed to add company {}: {}", company.name(), e.getMessage());
                    }
                }

                logger.info("Company data loading completed");
            }
        } catch (Exception e) {
            logger.error("Error loading companies from JSON file", e);
        }
    }
}
