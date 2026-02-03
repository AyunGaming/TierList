package fr.esgi.tier_list.domain.port;

import fr.esgi.tier_list.domain.TierList;

import java.util.List;
import java.util.Optional;

public interface TierListDataSourcePort {
    void save(TierList tierList);

    Optional<TierList> findById(String id);

    List<TierList> findAllByUserId(String userId);

    List<TierList> findAll();

    void deleteById(String id);
}
