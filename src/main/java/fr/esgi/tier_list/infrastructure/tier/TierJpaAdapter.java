package fr.esgi.tier_list.infrastructure.tier;

import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.port.TierDataSourcePort;
import fr.esgi.tier_list.infrastructure.company.CompanyJpaEntity;
import fr.esgi.tier_list.infrastructure.company.CompanyJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TierJpaAdapter implements TierDataSourcePort {

    private final TierJpaRepository tierJpaRepository;
    private final CompanyJpaRepository companyJpaRepository;

    public TierJpaAdapter(TierJpaRepository tierJpaRepository, CompanyJpaRepository companyJpaRepository) {
        this.tierJpaRepository = tierJpaRepository;
        this.companyJpaRepository = companyJpaRepository;
    }

    @Override
    public Optional<Tier> findByName(String name) {
        return tierJpaRepository.findByName(name).map(this::toDomain);
    }

    @Override
    @Transactional
    public void save(Tier tier) {
        TierJpaEntity entity = tierJpaRepository.findByName(tier.getName())
                .orElseGet(() -> {
                    TierJpaEntity newEntity = new TierJpaEntity();
                    newEntity.setId(java.util.UUID.randomUUID().toString());
                    return newEntity;
                });

        entity.setName(tier.getName());
        entity.setIndexPlacement(tier.getIndexPlacement());

        // Map companies from domain to entities
        List<CompanyJpaEntity> companyEntities = tier.getListCompany().stream()
                .map(c -> companyJpaRepository.findByName(c.getName()).orElse(null))
                .filter(c -> c != null)
                .collect(Collectors.toList());

        entity.setCompanies(companyEntities);

        tierJpaRepository.save(entity);
    }

    @Override
    public List<Tier> findAllOrderedByIndex() {
        return tierJpaRepository.findAllByOrderByIndexPlacementAsc().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Tier toDomain(TierJpaEntity entity) {
        Tier tier = new Tier(entity.getId(), entity.getName(), entity.getIndexPlacement());
        entity.getCompanies().forEach(c -> tier.getListCompany().add(new Company(c.getName(), c.getLogoUrl())));
        return tier;
    }
}
