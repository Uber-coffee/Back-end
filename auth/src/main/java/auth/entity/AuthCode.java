package auth.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "authcodes", uniqueConstraints = {
        @UniqueConstraint(columnNames = "session_id")})
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

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @PrePersist
    public void setRegistrationDate(){
        registrationDate = DateTime.now();
    }
}
