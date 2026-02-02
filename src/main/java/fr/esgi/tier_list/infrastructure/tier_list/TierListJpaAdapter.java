package fr.esgi.tier_list.infrastructure.tier_list;

import fr.esgi.tier_list.domain.TierList;
import fr.esgi.tier_list.domain.port.TierListDataSourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TierListJpaAdapter implements TierListDataSourcePort {

    private final TierListJpaRepository tierListJpaRepository;
    private final TierListMapper tierListMapper;

    @Override
    public void save(TierList tierList) {
        TierListJpaEntity entity = tierListMapper.toEntity(tierList);
        tierListJpaRepository.save(entity);
    }

    @Override
    public Optional<TierList> findById(String id) {
        return tierListJpaRepository.findById(id)
                .map(tierListMapper::toDomain);
    }

    @Override
    public List<TierList> findAllByUserId(String userId) {
        return tierListJpaRepository.findAllByUserId(userId).stream()
                .map(tierListMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        tierListJpaRepository.deleteById(id);
    }
}
