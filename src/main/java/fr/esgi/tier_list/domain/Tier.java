package fr.esgi.tier_list.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Tier {
    private final String id;
    private final String name;
    private final int indexPlacement;
    private final List<Company> listCompany;

    public Tier(String id, String name, int index_placement) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tier name cannot be empty");
        }

        if (index_placement <= 0) {
            throw new IllegalArgumentException("Tier placement must be superior to 0");
        }

        this.id = id;
        this.name = name;
        this.indexPlacement = index_placement;
        this.listCompany = new ArrayList<>();
    }
}
