package fr.esgi.tier_list.application.tier_list;

import fr.esgi.tier_list.domain.TierList;
import fr.esgi.tier_list.domain.port.TierListDataSourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TierListService {

    private final TierListDataSourcePort tierListDataSourcePort;

    public TierList create(String pdfName, String userId) {
        TierList tierList = new TierList(
                UUID.randomUUID().toString(),
                pdfName,
                new ArrayList<>(),
                userId);
        tierListDataSourcePort.save(tierList);
        return tierList;
    }

    public TierList getById(String id) {
        return tierListDataSourcePort.findById(id)
                .orElseThrow(() -> new RuntimeException("TierList not found"));
    }

    public List<TierList> getAllByUserId(String userId) {
        return tierListDataSourcePort.findAllByUserId(userId);
    }

    public void delete(String id) {
        tierListDataSourcePort.deleteById(id);
    }
}
