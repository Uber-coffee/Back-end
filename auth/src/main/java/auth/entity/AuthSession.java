package auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "authsessions", uniqueConstraints = {
        @UniqueConstraint(columnNames = "session_id")})
@Data
@NoArgsConstructor
public class AuthSession {
    @Id
    @Column(name = "session_id")
    private UUID sessionId;

    @Column (name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "registration_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime registrationDate;

    @PrePersist
    public void setRegistrationDate(){
        registrationDate = DateTime.now();
    }

    @OneToMany(mappedBy = "session")
    private List<AuthCode> authCodes = new ArrayList<>();

    public AuthSession(UUID uuid, String phoneNumber){
        this.sessionId = uuid;
        this.phoneNumber = phoneNumber;
    }
}
