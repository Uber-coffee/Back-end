package auth.service.auth;

import auth.entity.Customer;
import auth.exception.TokenException;
import auth.exception.UserAlreadyExistException;
import auth.payload.MobileSignupRequest;
import auth.repository.CustomerRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import auth.service.phone.PhoneVerifyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class MobileAuthService {

    private static final ModelMapper mapper = new ModelMapper();

    private final PhoneVerifyService phoneVerifyService;

    private final AuthenticationManager authenticationManager;

    private final AccessTokenProvider accessTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final CustomerRepository customerRepository;

    public MobileAuthService(@Qualifier("dummyPhoneVerifyService") PhoneVerifyService phoneVerifyService,
                             @Qualifier("mobileAuthenticationManagerBean") AuthenticationManager authenticationManager,
                             AccessTokenProvider accessTokenProvider,
                             RefreshTokenProvider refreshTokenProvider, CustomerRepository customerRepository) {
        this.phoneVerifyService = phoneVerifyService;
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.customerRepository = customerRepository;
    }

    public void login(String idToken, HttpServletResponse httpServletResponse) throws TokenException {
        final String phoneNumber = phoneVerifyService.verifyToken(idToken);

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                phoneNumber,
                ""
        ));

        final String accessToken = accessTokenProvider.createToken(auth);
        final String refreshToken = refreshTokenProvider.createToken(auth);
        accessTokenProvider.writeTokenToResponse(accessToken, httpServletResponse);
        refreshTokenProvider.writeTokenToResponse(refreshToken, httpServletResponse);
    }

    public void signup(MobileSignupRequest mobileSignupRequest,
                       HttpServletResponse httpServletResponse)
            throws TokenException, UserAlreadyExistException {
        final String phoneNumber = phoneVerifyService.verifyToken(mobileSignupRequest.getIdToken());

        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistException();
        }

        Customer customer = mapper.map(mobileSignupRequest, Customer.class);
        customer.setPhoneNumber(phoneNumber);
        customerRepository.save(customer);

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                phoneNumber,
                ""
        ));

        final String accessToken = accessTokenProvider.createToken(auth);
        final String refreshToken = refreshTokenProvider.createToken(auth);
        accessTokenProvider.writeTokenToResponse(accessToken, httpServletResponse);
        refreshTokenProvider.writeTokenToResponse(refreshToken, httpServletResponse);
    }
}
