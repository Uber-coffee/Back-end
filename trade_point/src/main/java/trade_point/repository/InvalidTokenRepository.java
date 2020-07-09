package trade_point.repository;

import trade_point.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Long> {

    boolean existsByToken(String token);
}
