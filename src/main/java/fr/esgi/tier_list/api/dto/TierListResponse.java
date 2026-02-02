package fr.esgi.tier_list.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TierListResponse {
    private String id;
    private String pdfName;
    private List<TierResponse> tiers;
    private String userId;
}
