package fr.esgi.tier_list.infrastructure.tier_list;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TierListJpaRepository extends JpaRepository<TierListJpaEntity, String> {
    List<TierListJpaEntity> findAllByUserId(String userId);
}
