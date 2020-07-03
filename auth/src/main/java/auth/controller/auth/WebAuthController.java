package auth.controller.auth;

import auth.config.swagger2.SwaggerMethodToDocument;
import auth.payload.WebLoginRequest;
import auth.service.auth.WebAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api
@RestController
@RequestMapping(value = "/w/auth")
public class WebAuthController {

    private final WebAuthService webAuthService;

    public WebAuthController(WebAuthService webAuthService) {
        this.webAuthService = webAuthService;
    }

    @SwaggerMethodToDocument
    @PostMapping(value = "/login")
    @ApiOperation(value = "Provide an e-mail and a password to log-in")
    public void login(@Valid @RequestBody WebLoginRequest webLoginRequest, HttpServletResponse httpServletResponse) {
        webAuthService.login(webLoginRequest, httpServletResponse);
    }
}