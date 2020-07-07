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
public class AuthSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column (name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "registration_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime registrationDate;

    @PrePersist
    public void setRegistrationDate(){
        registrationDate = DateTime.now();
    }
}
