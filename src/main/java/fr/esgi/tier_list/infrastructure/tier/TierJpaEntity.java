package fr.esgi.tier_list.infrastructure.tier;

import fr.esgi.tier_list.infrastructure.company.CompanyJpaEntity;
import fr.esgi.tier_list.infrastructure.tier_list.TierListJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "tier")
public class TierJpaEntity {

    @Id
    private String id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "index_placement", nullable = false)
    private int indexPlacement;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "tier_companies", joinColumns = @JoinColumn(name = "tier_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    private List<CompanyJpaEntity> companies = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_list_id")
    private TierListJpaEntity tierList;
}
