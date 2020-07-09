package auth.service;

import auth.entity.Role;
import auth.entity.User;
import auth.exception.UserAlreadyExistException;
import auth.payload.CreateUserRequest;
import auth.repository.UserRepository;
import auth.util.PasswordUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebUserService {

    private static final ModelMapper mapper = new ModelMapper();

    private final Logger log = LoggerFactory.getLogger(WebUserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public WebUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(CreateUserRequest createUserRequest, Role role) throws UserAlreadyExistException {
        if(userRepository.existsByEmail(createUserRequest.getEmail())){
            throw new UserAlreadyExistException();
        }
        User user = mapper.map(createUserRequest, User.class);
        user.setRoles(List.of(role));
        final String password = PasswordUtil.generatePassword();
        user.setPassword(passwordEncoder.encode(password));
        // TODO send credentials to email address
        userRepository.save(user);
        log.debug("Create user with credentials: email({}), password({})", user.getEmail(), password);
    }
}
