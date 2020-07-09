package auth.controller.auth;

import auth.config.swagger2.SwaggerMethodToDocument;
import auth.exception.TokenException;
import auth.exception.UserAlreadyExistException;
import auth.payload.MobileSignupRequest;
import auth.service.auth.MobileAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/m/auth")
@Api
public class MobileAuthController {

    private final MobileAuthService mobileAuthService;

    public MobileAuthController(MobileAuthService mobileAuthService) {
        this.mobileAuthService = mobileAuthService;
    }

    @SwaggerMethodToDocument
    @PostMapping(value = "/login")
    @ApiOperation(value = "Provide an Id Token of a customer to log-in")
    public void login(@RequestParam String idToken, HttpServletResponse httpServletResponse) throws TokenException {
        mobileAuthService.login(idToken, httpServletResponse);
    }

    @SwaggerMethodToDocument
    @PostMapping(value = "/signup")
    @ApiOperation(value = "Provide first name, Id Token and last name of a customer to sign-up")
    public void signup(@Valid @RequestBody MobileSignupRequest mobileSignupRequest,
                       HttpServletResponse httpServletResponse) throws TokenException, UserAlreadyExistException, IOException {
        mobileAuthService.signup(mobileSignupRequest, httpServletResponse);
    }
}