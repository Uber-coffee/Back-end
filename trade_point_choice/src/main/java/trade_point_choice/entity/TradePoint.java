package trade_point.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Table(name = "TradePoint")
@Data
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
