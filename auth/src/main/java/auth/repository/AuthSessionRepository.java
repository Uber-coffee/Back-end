package auth.repository;

import auth.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
    AuthSession findByPhoneNumber(String phoneNumber);
}
