package auth.controller;

import auth.config.swagger2.SwaggerMethodToDocument;
import auth.entity.Role;
import auth.exception.UserAlreadyExistException;
import auth.payload.CreateUserRequest;
import auth.service.WebUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/w/user")
public class WebUserController {

    private final WebUserService webUserService;

    public WebUserController(WebUserService webUserService) {
        this.webUserService = webUserService;
    }

    @SwaggerMethodToDocument
    @PostMapping(value = "/manager")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "To create a manager's account, provide an e-mail, first and last name")
    public void createManager(@Valid @RequestBody CreateUserRequest createUserRequest) throws UserAlreadyExistException {
        webUserService.createUser(createUserRequest, Role.ROLE_MANAGER);
    }

    @SwaggerMethodToDocument
    @PostMapping(value = "/seller")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
    @ApiOperation(value = "To create a seller's account, provide an e-mail, first and last name")
    public void createSeller(@Valid @RequestBody CreateUserRequest createUserRequest) throws UserAlreadyExistException {
        webUserService.createUser(createUserRequest, Role.ROLE_SELLER);
    }
}
