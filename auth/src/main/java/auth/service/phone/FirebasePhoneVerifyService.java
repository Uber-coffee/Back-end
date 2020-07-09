package auth.service.phone;

import auth.exception.TokenException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Service;

@Service
public class FirebasePhoneVerifyService implements PhoneVerifyService {

    @Override
    public String verifyToken(String idToken) throws TokenException {
        try {
            FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken);
            UserRecord user = FirebaseAuth.getInstance().getUser(token.getUid());
            final String phoneNumber = user.getPhoneNumber();
            if (phoneNumber == null) throw new TokenException();
            return phoneNumber;
        } catch (FirebaseAuthException e) {
            throw new TokenException();
        }
    }
}
