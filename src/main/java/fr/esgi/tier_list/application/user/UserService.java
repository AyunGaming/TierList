package fr.esgi.tier_list.application.user;

import fr.esgi.tier_list.domain.User;
import fr.esgi.tier_list.domain.port.UserDataSourcePort;
import fr.esgi.tier_list.domain.exceptions.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDataSourcePort userDataSourcePort;
    private final BCryptPasswordEncoder passwordEncoder;

    public User register(String name, String email, String username, String password) {
        if (userDataSourcePort.existsByEmail(email)) {
            throw new IllegalArgumentException("Cet email est déjà utilisé");
        }

        String hashedPassword = passwordEncoder.encode(password);

        User user = new User(
                UUID.randomUUID().toString(),
                name,
                email,
                username,
                hashedPassword);

        return userDataSourcePort.save(user);
    }

    public User login(String email, String rawPassword) {
        
        User user = userDataSourcePort.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Email incorrect"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new UserNotFoundException("Mot de passe incorrect");
        }

        return user;
    }

    public User getById(String id) {
        return userDataSourcePort.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé"));
    }
}