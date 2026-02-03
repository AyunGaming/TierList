package fr.esgi.tier_list.infrastructure.tier_list;

import fr.esgi.tier_list.infrastructure.tier.TierJpaEntity;
import fr.esgi.tier_list.infrastructure.user.UserJpaEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tier_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierListJpaEntity {

    @Id
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserJpaEntity user;

    @Builder.Default
    @OneToMany(mappedBy = "tierList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TierJpaEntity> tiers = new ArrayList<>();
}
