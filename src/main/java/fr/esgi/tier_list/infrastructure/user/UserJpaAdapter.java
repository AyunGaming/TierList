package fr.esgi.tier_list.infrastructure.user;

import fr.esgi.tier_list.domain.User;
import fr.esgi.tier_list.domain.port.UserDataSourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaAdapter implements UserDataSourcePort {
    private final UserJpaRepository repository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = new UserJpaEntity(
            user.getId(), 
            user.getName(), 
            user.getEmail(), 
            user.getUsername(),
            user.getPassword()
        );
        UserJpaEntity saved = repository.save(entity);
        return new User(saved.getId(), saved.getName(), saved.getEmail(), saved.getUsername(), saved.getPassword());
    }

    @Override
    public Optional<User> findById(String id) {
        return repository.findById(id)
                .map(e -> new User(e.getId(), e.getName(), e.getEmail(), e.getUsername(), e.getPassword()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(e -> new User(e.getId(), e.getName(), e.getEmail(), e.getUsername(), e.getPassword()));
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.findByEmail(email).isPresent();
    }
}