package fr.esgi.tier_list.infrastructure.tier_list;

import fr.esgi.tier_list.domain.Company;
import fr.esgi.tier_list.domain.Tier;
import fr.esgi.tier_list.domain.TierList;
import fr.esgi.tier_list.infrastructure.company.CompanyJpaEntity;
import fr.esgi.tier_list.infrastructure.tier.TierJpaEntity;
import fr.esgi.tier_list.infrastructure.user.UserJpaEntity;
import fr.esgi.tier_list.infrastructure.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TierListMapper {

    private final UserJpaRepository userJpaRepository;

    public TierList toDomain(TierListJpaEntity entity) {
        if (entity == null)
            return null;

        List<Tier> tiers = entity.getTiers().stream()
                .map(this::tierToDomain)
                .collect(Collectors.toList());

        return new TierList(
                entity.getId(),
                tiers,
                entity.getUser().getId());
    }

    public TierListJpaEntity toEntity(TierList domain) {
        if (domain == null)
            return null;

        UserJpaEntity user = userJpaRepository.findById(domain.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        TierListJpaEntity entity = TierListJpaEntity.builder()
                .id(domain.getId())
                .user(user)
                .tiers(new ArrayList<>())
                .build();

        if (domain.getList_tier() != null) {
            List<TierJpaEntity> tierEntities = domain.getList_tier().stream()
                    .map(t -> tierToEntity(t, entity))
                    .collect(Collectors.toList());
            entity.setTiers(tierEntities);
        }

        return entity;
    }

    private Tier tierToDomain(TierJpaEntity entity) {
        Tier tier = new Tier(entity.getId(), entity.getName(), entity.getIndexPlacement());
        if (entity.getCompanies() != null) {
            entity.getCompanies().forEach(c -> tier.getListCompany().add(new Company(c.getName(), c.getLogoUrl())));
        }
        return tier;
    }

    private TierJpaEntity tierToEntity(Tier domain, TierListJpaEntity tierListEntity) {
        List<CompanyJpaEntity> companies = domain.getListCompany().stream()
                .map(c -> new CompanyJpaEntity(c.getName(), c.getLogoUrl()))
                .collect(Collectors.toList());

        return TierJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .indexPlacement(domain.getIndexPlacement())
                .companies(companies)
                .tierList(tierListEntity)
                .build();
    }
}
