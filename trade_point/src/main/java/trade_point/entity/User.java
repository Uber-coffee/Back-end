package trade_point.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import trade_point.util.JsonJodaDateTimeSerializer;
import trade_point.util.Views;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Compact.class)
    private Long id;

    @Column(name = "first_name", nullable = false)
    @JsonView(Views.Compact.class)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @JsonView(Views.Compact.class)
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email
    @JsonView(Views.Compact.class)
    private String email;

    @Column(name = "phone_number", nullable = false)
    @JsonView(Views.Compact.class)
    private String phoneNumber;

    @Column(name = "registration_date", nullable = false, columnDefinition = "DATE")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonView(Views.Compact.class)
    @JsonSerialize(using = JsonJodaDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.OBJECT, pattern = "yyyy-MM-dd HH:mm:ss")
    private DateTime registrationDate;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "UserTradePoint",
            joinColumns = @JoinColumn(name = "User_id"),
            inverseJoinColumns = @JoinColumn(name = "TradePoint_id")
    )
    private Set<TradePoint> tradePoint = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}