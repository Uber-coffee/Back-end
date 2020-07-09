package auth.service.phone;

import auth.exception.TokenException;

public interface PhoneVerifyService {

    String verifyToken(String idToken) throws TokenException;
}
