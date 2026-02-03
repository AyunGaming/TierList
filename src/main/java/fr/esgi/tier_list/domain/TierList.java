package fr.esgi.tier_list.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class TierList {
    private final String id;
    private final List<Tier> list_tier;
    private final String user_id;

    public TierList(String id, List<Tier> list_tier, String user_id) {

        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Id cannot be empty");
        }

        if (user_id == null || user_id.isBlank()) {
            throw new IllegalArgumentException("User id cannot be empty");
        }

        this.id = id;
        this.list_tier = list_tier;
        this.user_id = user_id;
    }
}
