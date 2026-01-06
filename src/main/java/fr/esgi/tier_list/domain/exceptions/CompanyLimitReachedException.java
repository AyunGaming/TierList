package fr.esgi.tier_list.domain.exceptions;

public class CompanyLimitReachedException extends RuntimeException {

    public CompanyLimitReachedException() {
        super();
    }

    public CompanyLimitReachedException(String message) {
        super(message);
    }
}
