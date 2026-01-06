package fr.esgi.tier_list.application.company;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.exceptions.CompanyAlreadyExistsException;
import fr.esgi.tier_list.domain.exceptions.CompanyLimitReachedException;
import fr.esgi.tier_list.domain.port.CompanyDataSourcePort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private static final int MAX_COMPANIES = 10;

    private final CompanyDataSourcePort companyDataSourcePort;

    public CompanyService(CompanyDataSourcePort companyDataSourcePort) {
        this.companyDataSourcePort = companyDataSourcePort;
    }

    public void addCompany(String name, String logoUrl) {

        if (companyDataSourcePort.findByName(name).isPresent()) {
            throw new CompanyAlreadyExistsException("Company with name " + name + " already exists");
        }

        if (companyDataSourcePort.count() >= MAX_COMPANIES) {
            throw new CompanyLimitReachedException("Company limit reached");
        }

        Company company = new Company(
                name,
                logoUrl
        );

        companyDataSourcePort.save(company);
    }

    public List<CompanyResponse> listCompanies() {
        return companyDataSourcePort.findAll();
    }
}
