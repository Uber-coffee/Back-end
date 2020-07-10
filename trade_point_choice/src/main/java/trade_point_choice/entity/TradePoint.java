package trade_point.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "TradePoint")

@Getter
@Setter
@NoArgsConstructor
public class TradePoint {

    @Id
    @Column(name = "ID", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "latitude", unique = true, nullable = false)
    private Double latitude;

    @Column(name = "longitude", unique = true, nullable = false)
    private Double longitude;

}
