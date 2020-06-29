package auth.service.phone;

import auth.exception.TokenException;
import org.springframework.stereotype.Service;

@Service
public class DummyPhoneVerifyService implements PhoneVerifyService {
    @Override
    public String verifyToken(String idToken) throws TokenException {
        return idToken;
    }
}
