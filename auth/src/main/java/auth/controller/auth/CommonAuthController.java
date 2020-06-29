package auth.controller.auth;

import auth.exception.TokenException;
import auth.exception.UserNotFoundException;
import auth.service.auth.CommonAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/auth")
public class CommonAuthController {

    private final CommonAuthService commonAuthService;

    public CommonAuthController(CommonAuthService commonAuthService) {
        this.commonAuthService = commonAuthService;
    }

    @PostMapping(value = "/refresh")
    public void refresh(HttpServletRequest httpServletRequest,
                        HttpServletResponse httpServletResponse)
            throws TokenException, UserNotFoundException {
        commonAuthService.refresh(httpServletRequest, httpServletResponse);
    }

    @PostMapping(value = "/logout")
    public void logout(HttpServletRequest httpServletRequest) throws TokenException {
        commonAuthService.logout(httpServletRequest);
    }
}
