package fr.esgi.tier_list.infrastructure.company;

import fr.esgi.tier_list.api.dto.CompanyResponse;
import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.port.CompanyDataSourcePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CompanyJpaAdapter implements CompanyDataSourcePort {
    private final CompanyJpaRepository companyJpaRepository;

    public CompanyJpaAdapter(CompanyJpaRepository companyJpaRepository) {
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Optional<CompanyResponse> findByName(String name) {
        return companyJpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    public long count() {
        return companyJpaRepository.count();
    }

    @Override
    public void save(Company company) {
        companyJpaRepository.save(toEntity(company));
    }

    @Override
    public List<CompanyResponse> findAll() {
        return companyJpaRepository.findAll().stream()
                .map(this::toDomain)
                .toList();
    }

    private CompanyResponse toDomain(CompanyJpaEntity e) {
        return new CompanyResponse(e.getId(), e.getName(), e.getLogoUrl());
    }

    private CompanyJpaEntity toEntity(Company c) {
        CompanyJpaEntity e = new CompanyJpaEntity();
        e.setName(c.getName());
        e.setLogoUrl(c.getLogoUrl());
        return e;
    }
}
