package auth.service.phone;

import auth.exception.SMSBalanceException;
import auth.exception.SMSDeliveryException;
import auth.exception.SMSVerifyException;

public interface PhoneVerifyServiceSMS {
    boolean sendVerifyMessage(String phoneNumber, String code) throws SMSDeliveryException, SMSVerifyException, SMSBalanceException;
}
