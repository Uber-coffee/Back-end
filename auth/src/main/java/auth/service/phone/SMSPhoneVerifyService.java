package auth.service.phone;

import auth.exception.handle.ExceptionsSMS.SMSDeliveryException;
import auth.exception.handle.ExceptionsSMS.SMSVerifyException;
import auth.service.smsc.Smsc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SMSPhoneVerifyService implements PhoneVerifyServiceSMS{
    @Value(value = "${application.smsc.login}")
    private String login;
    @Value(value = "${application.smsc.password}")
    private String password;


    @Override
    public boolean sendVerifyMessage(String phoneNumber, String code) throws SMSDeliveryException, SMSVerifyException {
        Smsc smsc = new Smsc(login, password);

        //System.out.println(phoneNumber + "  :  " + code);

        String[] retSend = smsc.send_sms(phoneNumber.substring(1), "Ваш пароль: " + code, 0, "", "", 0, "", "");

        if (retSend.length < 3){throw new SMSVerifyException();}

        String[] retStatus = smsc.get_status(Integer.parseInt(retSend[0]), phoneNumber.substring(1), 3);
        String retBalance = smsc.get_balance();

        if (Math.round(Double.parseDouble(retBalance)) < 10){
            System.out.println("you're running out of MUHNEY");
        }

        if (!retStatus[2].equals("200")){throw new SMSDeliveryException();}

        return true;
    }
}