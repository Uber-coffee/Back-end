package auth.controller.auth;

import auth.payload.WebLoginRequest;
import auth.service.auth.WebAuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/w/auth")
public class WebAuthController {

    private final WebAuthService webAuthService;

    public WebAuthController(WebAuthService webAuthService) {
        this.webAuthService = webAuthService;
    }

    @PostMapping(value = "/login")
    public void login(@Valid @RequestBody WebLoginRequest webLoginRequest, HttpServletResponse httpServletResponse) {
        webAuthService.login(webLoginRequest, httpServletResponse);
    }
}
