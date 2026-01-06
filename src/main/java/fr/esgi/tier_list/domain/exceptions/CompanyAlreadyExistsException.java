package fr.esgi.tier_list.domain.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException {
    public CompanyAlreadyExistsException() {
        super();
    }

    public CompanyAlreadyExistsException(String message) {
        super(message);
    }
}
