package auth.controller;

import auth.config.swagger2.SwaggerMethodToDocument;
import auth.exception.TokenException;
import auth.payload.MobileSignupRequest;
import auth.service.auth.MobileAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    @ApiOperation(value = "Provide phone, session_Id and verification code")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sign-up request accepted"),
            @ApiResponse(code = 201, message = "Customer created and registered"),
            @ApiResponse(code = 202, message = "Customer registered"),
            @ApiResponse(code = 510, message = "Sessions overflow for a single phone number"),
            @ApiResponse(code = 406, message = "No session_id was provided"),
            @ApiResponse(code = 422, message = "Session_Id doesn't match the provided phone number"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 409, message = "Session_ID can not be processed")
    })
    public void signup(@Valid @RequestBody MobileSignupRequest mobileSignupRequest,
                       HttpServletResponse httpServletResponse) throws TokenException{
        mobileAuthService.signup(mobileSignupRequest, httpServletResponse);
    }
}