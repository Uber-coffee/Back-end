package auth.service.auth;

import auth.entity.AuthCode;
import auth.entity.AuthSession;
import auth.entity.Customer;
import auth.exception.*;
import auth.exception.handle.ExceptionsSMS.*;
import auth.payload.MobileSignupRequest;
import auth.repository.AuthCodeRepository;
import auth.repository.AuthSessionRepository;
import auth.repository.CustomerRepository;
import auth.security.token.AccessTokenProvider;
import auth.security.token.RefreshTokenProvider;
import auth.service.phone.PhoneVerifyService;
import auth.service.phone.PhoneVerifyServiceSMS;
import org.joda.time.DateTime;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final AuthSessionRepository authSessionRepository;

    private final AuthCodeRepository authCodeRepository;

    @Value(value = "${application.smsc.session_keep_alive}")
    private Integer AuthSessionExpirationTime;

    @Value(value = "${application.smsc.code_keep_alive}")
    private Integer AuthCodeExpirationTime;

    @Value(value = "${application.smsc.sessions_per_phone}")
    private Integer AuthSessionsPerPhone;

    @Value(value = "${application.smsc.codes_per_session}")
    private Integer AuthCodesPerSession;

    public MobileAuthService(@Qualifier("dummyPhoneVerifyService") PhoneVerifyService phoneVerifyService,
                             @Qualifier("SMSPhoneVerifyService") PhoneVerifyServiceSMS phoneVerifyServiceSMS,
                             @Qualifier("mobileAuthenticationManagerBean") AuthenticationManager authenticationManager,
                             AccessTokenProvider accessTokenProvider,
                             RefreshTokenProvider refreshTokenProvider, CustomerRepository customerRepository,
                             AuthSessionRepository authSessionRepository, AuthCodeRepository authCodeRepository) {
        this.phoneVerifyService = phoneVerifyService;
        this.phoneVerifyServiceSMS = phoneVerifyServiceSMS;
        this.authenticationManager = authenticationManager;
        this.accessTokenProvider = accessTokenProvider;
        this.refreshTokenProvider = refreshTokenProvider;
        this.customerRepository = customerRepository;
        this.authSessionRepository = authSessionRepository;
        this.authCodeRepository = authCodeRepository;
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
            throws TokenException, UserAlreadyExistException, IOException {

        final String phoneNumber = phoneVerifyService.verifyToken(mobileSignupRequest.getPhoneNumber());

        if (customerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistException();
        }

        if (authSessionRepository.findByPhoneNumber(phoneNumber).size() < 1){
            initiateRegistrationSession(httpServletResponse, phoneNumber);

        } else {
            AuthSession buffer = authSessionRepository.findBySessionId(mobileSignupRequest.getSessionID());

            if (buffer == null){
                httpServletResponse.sendError(406);
                throw new BadCredentialsException("You are supposed to send session_id, aren't you");
            } else {
                if(buffer.getPhoneNumber().equals(phoneNumber)){
                    initiateValidationSession(buffer, httpServletResponse, mobileSignupRequest);

                }else{
                    httpServletResponse.sendError(422);
                    throw new IllegalStateException();
                }
            }
        }
    }

    private void initiateValidationSession(AuthSession authSession, HttpServletResponse httpServletResponse, MobileSignupRequest mobileSignupRequest) throws IOException {
        List<AuthSession> authSessionList = authSessionRepository.findByPhoneNumber(authSession.getPhoneNumber());

        if (authSessionList.stream().filter(this::isAuthSessionValid).count() < this.AuthSessionsPerPhone){
            List<AuthSession> validAuthSessionList = authSessionList.stream().filter(this::isAuthSessionValid).collect(Collectors.toList());
            System.out.println(validAuthSessionList.toString());

            List<AuthCode> codes = validAuthSessionList.stream().map(AuthSession::getAuthCodes).flatMap(Collection::stream).collect(Collectors.toList());
            System.out.println(codes.toString());

            boolean codeCheck = initiateCodeCheck(authSession, httpServletResponse, validAuthSessionList, codes, mobileSignupRequest);

            if (!codeCheck){
                initiateExtraMessageSession(authSession, httpServletResponse, authSession.getPhoneNumber(), mobileSignupRequest);
            }
        } else {
            sessionsPerCustomerOverflow(httpServletResponse);
        }
    }

    private void sessionsPerCustomerOverflow(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendError(510);
        System.out.println("Too many sessions for a single customer");
    }

    private boolean initiateCodeCheck(AuthSession authSession, HttpServletResponse httpServletResponse, List<AuthSession> validAuthSessionList, List<AuthCode> codes, MobileSignupRequest mobileSignupRequest) {

        for (AuthSession session: validAuthSessionList) {
            for (AuthCode authcode: codes) {
                if ((session.getSessionId() == authSession.getSessionId())
                        && (authcode.getSmsCode().equals(mobileSignupRequest.getVerifyCode()))){
                    if (isAuthCodeValid(authcode)){
                        Customer customer = new Customer();
                        customer.setPhoneNumber(authSession.getPhoneNumber());
                        customerRepository.save(customer);

                        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                                authSession.getPhoneNumber(),
                                ""
                        ));

                        final String accessToken = accessTokenProvider.createToken(auth);
                        final String refreshToken = refreshTokenProvider.createToken(auth);

                        accessTokenProvider.writeTokenToResponse(accessToken, httpServletResponse);
                        refreshTokenProvider.writeTokenToResponse(refreshToken, httpServletResponse);

                       return true;
                    }
                }
            }
        }
        return false;
    }

    private void initiateExtraMessageSession(AuthSession authSession, HttpServletResponse httpServletResponse, String phoneNumber, MobileSignupRequest mobileSignupRequest) throws IOException {
        if (authSessionRepository.countByPhoneNumber(mobileSignupRequest.getPhoneNumber()) < this.AuthSessionsPerPhone){
            if (authCodeRepository.countBySession(authSession) < this.AuthCodesPerSession){
                final String regCode = generateCodeForService(phoneNumber);

                AuthCode authCode = new AuthCode(regCode, authSession);
                authCode.setRegistrationDate();
                System.out.println(authCode.getRegistrationDate());
                authCodeRepository.save(authCode);

                httpServletResponse.addHeader("session_id", mobileSignupRequest.getSessionID().toString());
            } else {
                initiateRegistrationSession(httpServletResponse, phoneNumber);
            }
        } else {
            sessionsPerCustomerOverflow(httpServletResponse);
        }
    }

    private void initiateRegistrationSession(HttpServletResponse httpServletResponse, String phoneNumber) throws IOException {
        final String regCode = generateCodeForService(phoneNumber);
        System.out.println(regCode);
        final UUID currentSessionID = UUID.randomUUID();

        boolean smsResult = sendMessage(phoneNumber, regCode);

        if (smsResult) {
            System.out.println(currentSessionID.toString());

            AuthSession authSession = new AuthSession(currentSessionID, phoneNumber);

            saveNewAuthCodeWithNewSession(regCode, authSession);
            httpServletResponse.addHeader("session_id", currentSessionID.toString());
        } else {
            httpServletResponse.sendError(500);
        }
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

    private static String generateCodeForService(String phoneNumber){
        return phoneNumber.substring(phoneNumber.length() - 4);
    }

    private boolean isAuthSessionValid(AuthSession authSession){
        return authSession.getRegistrationDate().plusSeconds(this.AuthSessionExpirationTime).isAfter(DateTime.now());
    }

    private boolean isAuthCodeValid(AuthCode authCode){
        return authCode.getRegistrationDate().plusSeconds(this.AuthCodeExpirationTime).isAfter(DateTime.now());
    }

    private void saveNewAuthCodeWithNewSession(String regCode, AuthSession authSession){
        AuthCode authCode = new AuthCode(regCode, authSession);
        authSession.getAuthCodes().add(authCode);

        this.authSessionRepository.save(authSession);
        this.authCodeRepository.save(authCode);
    }

    private boolean sendMessage(String phoneNumber, String regCode){
        boolean smsResult = false;
        try {
            smsResult = this.phoneVerifyServiceSMS.sendVerifyMessage(phoneNumber, regCode);
        }catch (SMSForbiddenException e){ log.warn("SMS is forbidden!");
        }catch (SMSParametersException e){ log.warn("SMS not correct!");
        }catch (SMSBalanceException e){ log.warn("Account is running out of money!");
        }catch (SMSServiceOverloadException e){ log.warn("Service is overloaded!");
        }catch (SMSDateFormatException e){ log.warn("Date format is wrong!");
        }catch (SMSCredentialsException e){ log.warn("Wrong credentials are given!");
        }catch (SMSPhoneFormatException e){ log.warn("Wrong phone format!");
        }catch (SMSDeliveryDeniedException e){ log.warn("Delivery got denied!");
        }catch (SMSFloodException e){ log.warn("Don't flood SMS service!");
        }return smsResult;
    }
}