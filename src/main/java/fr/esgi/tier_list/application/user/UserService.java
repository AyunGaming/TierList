package fr.esgi.tier_list.application.user;

import fr.esgi.tier_list.domain.User;
import fr.esgi.tier_list.domain.port.UserDataSourcePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDataSourcePort userDataSourcePort;

    public User register(String name, String email, String username) {
        User user = new User(UUID.randomUUID().toString(), name, email, username);
        return userDataSourcePort.save(user);
    }

    public User getById(String id) {
        return userDataSourcePort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}