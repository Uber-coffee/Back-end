package auth.config;

import auth.entity.Role;
import auth.entity.User;
import auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class HardcoreAdminConfig {

    @Value(value = "${application.admin.email}")
    private String email;

    @Value(value = "${application.admin.firstName}")
    private String firstName;

    @Value(value = "${application.admin.lastName}")
    private String lastName;

    @Value(value = "${application.admin.password}")
    private String password;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public HardcoreAdminConfig(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        if(!userRepository.existsByEmail(email)) {
            userRepository.save(
                    new User(
                            firstName,
                            lastName,
                            email,
                            passwordEncoder.encode(password),
                            List.of(Role.ROLE_ADMIN)
                    )
            );
        }
    }
}
