package auth.repository;

import auth.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Long> {

    boolean existsByToken(String token);
}
