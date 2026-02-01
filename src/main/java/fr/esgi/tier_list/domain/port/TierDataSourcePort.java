package fr.esgi.tier_list.domain.port;

import fr.esgi.tier_list.domain.Tier;

import java.util.List;
import java.util.Optional;

public interface TierDataSourcePort {
    Optional<Tier> findByName(String name);

    void save(Tier tier);

    List<Tier> findAllOrderedByIndex();
}
