package fr.esgi.tier_list.domain;

import lombok.Getter;

@Getter
public class Company {
    private final String name;
    private final String logoUrl;

    public Company(String name, String logoUrl) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Company name cannot be empty");
        }

        if (logoUrl == null || logoUrl.isBlank()) {
            throw new IllegalArgumentException("Logo URL cannot be empty");
        }

        this.name = name;
        this.logoUrl = logoUrl;
    }
}
