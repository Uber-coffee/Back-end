package auth.service.phone;
import auth.exception.handle.ExceptionsSMS.*;

public interface PhoneVerifyServiceSMS {
    boolean sendVerifyMessage(String phoneNumber, String code) throws SMSParametersException, SMSCredentialsException,
            SMSForbiddenException, SMSBalanceException, SMSServiceOverloadException, SMSFloodException,
            SMSDateFormatException, SMSPhoneFormatException, SMSDeliveryDeniedException;
}
