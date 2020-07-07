package auth.service.auth;

import auth.entity.Customer;
import auth.exception.*;
import auth.payload.MobileSignupRequest;
import auth.repository.CustomerRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import auth.service.phone.PhoneVerifyService;
import auth.service.phone.PhoneVerifyServiceSMS;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

@Service
public class MobileAuthService {

    private static final Logger log = LoggerFactory.getLogger(MobileAuthService.class);

    private static final ModelMapper mapper = new ModelMapper();

    private final PhoneVerifyService phoneVerifyService;

    private final PhoneVerifyServiceSMS phoneVerifyServiceSMS;

    private final AuthenticationManager authenticationManager;

    private final AccessTokenProvider accessTokenProvider;

    private final RefreshTokenProvider refreshTokenProvider;

    private final CustomerRepository customerRepository;

    public MobileAuthService(@Qualifier("dummyPhoneVerifyService") PhoneVerifyService phoneVerifyService,
                             @Qualifier("SMSPhoneVerifyService") PhoneVerifyServiceSMS phoneVerifyServiceSMS,
                             @Qualifier("mobileAuthenticationManagerBean") AuthenticationManager authenticationManager,
                             AccessTokenProvider accessTokenProvider,
                             RefreshTokenProvider refreshTokenProvider, CustomerRepository customerRepository) {
        this.phoneVerifyService = phoneVerifyService;
        this.phoneVerifyServiceSMS = phoneVerifyServiceSMS;
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.customerRepository = customerRepository;
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
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

    public void signup(MobileSignupRequest mobileSignupRequest, HttpServletResponse httpServletResponse)
            throws TokenException, UserAlreadyExistException {

        final String phoneNumber = phoneVerifyService.verifyToken(mobileSignupRequest.getIdToken());
        final String regCode = generateCodeForService();

        boolean smsResult = false;

        try {
            smsResult = phoneVerifyServiceSMS.sendVerifyMessage(phoneNumber, regCode);
        }catch (SMSVerifyException e){
            log.warn("SMS code was not sent!");
        }catch (SMSDeliveryException e){
            log.warn("SMS was not delivered!");
        }catch (SMSBalanceException e){
            log.warn("Your account is running out of MAHNEY");
        }



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

    private static String generateCodeForService(){
        final String digits = "0123456789";
        String result = "";

        for (int i = 0; i < 4; i++) {
            int pos = (int) (Math.random() * 10);
            if (pos == 10){pos = 0;}

            result += digits.charAt(pos);
        }

        return result;
    }
}