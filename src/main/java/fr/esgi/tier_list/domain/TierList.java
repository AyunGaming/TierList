package fr.esgi.tier_list.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class TierList {
    private final String id;
    private final String pdf_name;
    private final List<Tier> list_tier;
    private final String user_id;

    public TierList(String id, String pdf_name, List<Tier> list_tier, String user_id) {
        if (pdf_name == null || pdf_name.isBlank()) {
            throw new IllegalArgumentException("Pdf name cannot be empty");
        }

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be empty");
        }

        if (user_id == null || user_id.isBlank()) {
            throw new IllegalArgumentException("User id cannot be empty");
        }

        this.id = id;
        this.pdf_name = pdf_name;
        this.list_tier = list_tier;
        this.user_id = user_id;
    }
}
