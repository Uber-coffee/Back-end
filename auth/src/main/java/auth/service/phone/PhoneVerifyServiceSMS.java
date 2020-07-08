package auth.service.phone;

import auth.exception.handle.ExceptionsSMS.SMSDeliveryException;
import auth.exception.handle.ExceptionsSMS.SMSVerifyException;

public interface PhoneVerifyServiceSMS {
    boolean sendVerifyMessage(String phoneNumber, String code) throws SMSDeliveryException, SMSVerifyException;
}
