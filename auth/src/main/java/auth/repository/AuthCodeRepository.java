package auth.repository;

import auth.entity.AuthCode;
import auth.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    int countBySession(AuthSession authSession);
}