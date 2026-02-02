package fr.esgi.tier_list.domain.port;

import fr.esgi.tier_list.domain.User;
import java.util.Optional;

public interface UserDataSourcePort {

    User save(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}