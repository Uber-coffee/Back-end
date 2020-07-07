package auth.service.auth;

import auth.entity.Customer;
import auth.entity.Role;
import auth.exception.*;
import auth.payload.MobileSignupRequest;
import auth.repository.CustomerRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import auth.service.phone.PhoneVerifyService;
import auth.service.phone.PhoneVerifyServiceSMS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MobileAuthServiceTest {

    private PhoneVerifyService phoneVerifyService;

    private PhoneVerifyServiceSMS phoneVerifyServiceSMS;

    private MobileAuthService mobileAuthService;

    private AuthenticationManager authenticationManager;

    private AccessTokenProvider accessTokenProvider;

    private RefreshTokenProvider refreshTokenProvider;

    private CustomerRepository customerRepository;

    private Map<String, Customer> customers;

    @BeforeEach
    public void init() throws TokenException, SMSDeliveryException, SMSVerifyException, SMSBalanceException {
        initPhoneVerifyService();
        initAuthenticationManager();
        initAccessTokenProvider();
        initPhoneVerifyServiceSMS();
        initRefreshToken();
        initCustomerRepository();
        customers = new HashMap<>();
        mobileAuthService = new MobileAuthService(
                phoneVerifyService,
                phoneVerifyServiceSMS,
                authenticationManager,
                accessTokenProvider,
                refreshTokenProvider,
                customerRepository);
    }

    public void initPhoneVerifyService() throws TokenException {
        phoneVerifyService = mock(PhoneVerifyService.class);
        when(phoneVerifyService.verifyToken(anyString())).then(i -> i.getArgument(0));
    }

    public void initPhoneVerifyServiceSMS() throws SMSVerifyException, SMSDeliveryException, SMSBalanceException {
        phoneVerifyServiceSMS = mock(PhoneVerifyServiceSMS.class);
        phoneVerifyServiceSMS.sendVerifyMessage("+79004443322", "1488");
    }

    public void initAuthenticationManager() {
        authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any(Authentication.class))).then(i -> {
            Authentication a = i.getArgument(0);
            return new UsernamePasswordAuthenticationToken(
                    a.getPrincipal(),
                    a.getCredentials(),
                    List.of(Role.ROLE_CUSTOMER)
            );
        });
    }

    public void initAccessTokenProvider() {
        accessTokenProvider = mock(AccessTokenProvider.class);
        when(accessTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("auth", token);
            return null;
        }).when(accessTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    public void initRefreshToken() {
        refreshTokenProvider = mock(RefreshTokenProvider.class);
        when(refreshTokenProvider.createToken(any(Authentication.class))).then(i -> {
            Authentication auth = i.getArgument(0);
            return auth.getPrincipal().toString() +
                    "/" +
                    auth.getCredentials() +
                    "/" +
                    auth.getAuthorities().toArray()[0].toString();
        });
        doAnswer(i -> {
            String token = i.getArgument(0);
            HttpServletResponse httpServletResponse = i.getArgument(1);
            httpServletResponse.addHeader("ref", token);
            return null;
        }).when(refreshTokenProvider).writeTokenToResponse(anyString(), any(HttpServletResponse.class));
    }

    public void initCustomerRepository() {
        customerRepository = mock(CustomerRepository.class);
        when(customerRepository.existsByPhoneNumber(anyString())).then(i ->
                customers.get(i.getArgument(0)) != null
        );
        when(customerRepository.save(any(Customer.class))).then(i -> {
            Customer customer = i.getArgument(0);
            customers.put(customer.getPhoneNumber(), customer);
            return null;
        });
    }

    @Test
    void login() throws TokenException {
        Customer customer = new Customer();
        customer.setPhoneNumber("89500190736");
        customer.setFirstName("test");
        customer.setLastName("test");
        customerRepository.save(customer);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        mobileAuthService.login("89500190736", httpServletResponse);
        assertNotNull(httpServletResponse.getHeader("auth"));
        assertNotNull(httpServletResponse.getHeader("ref"));
    }

    @Test
    void loginException() {
        Customer customer = new Customer();
        customer.setPhoneNumber("89500190736");
        customer.setFirstName("test");
        customer.setLastName("test");
        customerRepository.save(customer);
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        assertThrows(UserAlreadyExistException.class,
                () -> mobileAuthService.signup(
                        new MobileSignupRequest(
                                "test",
                                "test",
                                "89500190736"),
                        httpServletResponse));
    }

    @Test
    void signup() throws TokenException, UserAlreadyExistException {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        mobileAuthService.signup(new MobileSignupRequest("test", "test", "89500190736"), httpServletResponse);
        assertEquals(1, customers.entrySet().size());
        assertNotNull(httpServletResponse.getHeader("auth"));
        assertNotNull(httpServletResponse.getHeader("ref"));
    }
}