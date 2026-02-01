package fr.esgi.tier_list.domain.exceptions.tier;

public class TierNotFoundException extends RuntimeException {
    public TierNotFoundException(String message) {
        super(message);
    }
}
