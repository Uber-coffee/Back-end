package auth.controller.auth;

import auth.exception.TokenException;
import auth.exception.UserAlreadyExistException;
import auth.payload.MobileSignupRequest;
import auth.service.auth.MobileAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/m/auth")
public class MobileAuthController {

    private final MobileAuthService mobileAuthService;

    public MobileAuthController(MobileAuthService mobileAuthService) {
        this.mobileAuthService = mobileAuthService;
    }

    @PostMapping(value = "/login")
    public void login(@RequestParam String idToken, HttpServletResponse httpServletResponse) throws TokenException {
        mobileAuthService.login(idToken, httpServletResponse);
    }

    @PostMapping(value = "/signup")
    public void signup(@Valid @RequestBody MobileSignupRequest mobileSignupRequest,
                       HttpServletResponse httpServletResponse) throws TokenException, UserAlreadyExistException {
        mobileAuthService.signup(mobileSignupRequest, httpServletResponse);
    }
}
