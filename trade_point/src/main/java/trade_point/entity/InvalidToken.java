package trade_point.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "invalid_tokens")
@Data
@NoArgsConstructor
public class InvalidToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime expireDate;

    public InvalidToken(String token, DateTime expireDate) {
        this.token = token;
        this.expireDate = expireDate;
    }
}
