package auth.controller;

import auth.entity.Role;
import auth.exception.UserAlreadyExistException;
import auth.payload.CreateUserRequest;
import auth.service.WebUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/w/user")
public class WebUserController {

    private final WebUserService webUserService;

    public WebUserController(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @PostMapping(value = "/manager")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public void createManager(@Valid @RequestBody CreateUserRequest createUserRequest) throws UserAlreadyExistException {
        webUserService.createUser(createUserRequest, Role.ROLE_MANAGER);
    }

    @PostMapping(value = "/seller")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_SELLER')")
    public void createSeller(@Valid @RequestBody CreateUserRequest createUserRequest) throws UserAlreadyExistException {
        webUserService.createUser(createUserRequest, Role.ROLE_SELLER);
    }
}
