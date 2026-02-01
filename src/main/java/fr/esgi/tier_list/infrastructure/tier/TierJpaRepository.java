package fr.esgi.tier_list.infrastructure.tier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TierJpaRepository extends JpaRepository<TierJpaEntity, String> {
    Optional<TierJpaEntity> findByName(String name);

    List<TierJpaEntity> findAllByOrderByIndexPlacementAsc();
}
