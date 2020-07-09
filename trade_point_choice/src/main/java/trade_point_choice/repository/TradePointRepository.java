package trade_point.repository;

import trade_point.entity.TradePoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradePointRepository extends JpaRepository<TradePoint,Long> {

    List<TradePoint> findByLongitudeBetweenAndLatitudeBetween(
            double leftLongitude,
            double rightLongitude,
            double leftLatitude,
            double rightLatitude);
}
