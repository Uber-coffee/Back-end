package auth.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "authcodes")
@Data
@NoArgsConstructor
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sms_code", nullable = false)
    private String smsCode;

    @Column(name = "registration_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime registrationDate;

    @ManyToOne
    @JoinColumn (name = "session_FK", nullable = false)
    private AuthSession session;

    @PrePersist
    public void setRegistrationDate(){
        registrationDate = DateTime.now();
    }

    public AuthCode( String smsCode, AuthSession authSession){
        this.session = authSession;
        this.smsCode = smsCode;
    }

    @Override
    public String toString() {
        return "AuthCode{" +
                "id=" + id +
                ", smsCode='" + smsCode + '\'' +
                ", registrationDate=" + registrationDate +
                ", session=" + session.getSessionId() +
                '}';
    }
}