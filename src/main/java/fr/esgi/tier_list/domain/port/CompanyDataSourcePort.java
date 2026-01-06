package fr.esgi.tier_list.domain.port;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDataSourcePort {

    Optional<CompanyResponse> findByName(String name);

    long count();

    void save(Company company);

    List<CompanyResponse> findAll();
}
