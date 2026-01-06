package fr.esgi.tier_list.infrastructure.company;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyJpaRepository extends JpaRepository<CompanyJpaEntity, String> {
    Optional<CompanyJpaEntity> findByName(String name);
}
