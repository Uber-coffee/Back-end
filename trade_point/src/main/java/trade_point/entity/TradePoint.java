package trade_point.entity;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import trade_point.util.Views;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TradePoint")
@Data
@NoArgsConstructor
public class TradePoint {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Compact.class)
    private Long id;

    @Column(name = "name", nullable = false)
    @JsonView(Views.Compact.class)
    private String name;

    @Column(name = "address", nullable = false)
    @JsonView(Views.Compact.class)
    private String address;

    @Column(name = "latitude", nullable = false)
    @JsonView(Views.Compact.class)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    @JsonView(Views.Compact.class)
    private Double longitude;

    @Column(name = "active", unique = false, nullable = false)
    @JsonView(Views.Compact.class)
    private Boolean isActive;

    @ManyToMany(mappedBy = "tradePoint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradePoint)) return false;
        TradePoint that = (TradePoint) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

