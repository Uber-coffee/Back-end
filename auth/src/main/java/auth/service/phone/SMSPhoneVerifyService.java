package auth.service.phone;

import auth.exception.handle.ExceptionsSMS.*;
import auth.service.smsc.Smsc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.IllegalFormatException;
import java.util.zip.DataFormatException;

@Service
public class SMSPhoneVerifyService implements PhoneVerifyServiceSMS{
    @Value(value = "${application.smsc.login}")
    private String login;
    @Value(value = "${application.smsc.password}")
    private String password;


    @Override
    public boolean sendVerifyMessage(String phoneNumber, String code) throws SMSParametersException, SMSCredentialsException,
            SMSBalanceException, SMSServiceOverloadException, SMSDateFormatException, SMSFloodException,
            SMSForbiddenException, SMSPhoneFormatException, SMSDeliveryDeniedException{
        Smsc smsc = new Smsc(login, password);

        String[] retSend = smsc.send_sms(phoneNumber.substring(1), "Ваш пароль: " + code, 0, "", "", 0, "", "");

        String[] retStatus = smsc.get_status(Integer.parseInt(retSend[0]), phoneNumber.substring(1), 1);

        if (retStatus.length < 1 ){
            return false;
        }

        if (!retStatus[2].equals("200")){
            switch (retSend[1]){
                case ("-1"):
                    throw new SMSParametersException();
                case ("-2"):
                    throw new SMSCredentialsException();
                case ("-3"):
                    throw new SMSBalanceException();
                case ("-4"):
                    throw new SMSServiceOverloadException();
                case ("-5"):
                    throw new SMSDateFormatException();
                case ("-6"):
                    throw new SMSForbiddenException();
                case ("-7"):
                    throw new SMSPhoneFormatException();
                case ("-8"):
                    throw new SMSDeliveryDeniedException();
                case ("-9"):
                    throw new SMSFloodException();
            }
        }
        return true;
    }
}