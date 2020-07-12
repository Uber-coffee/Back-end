package auth.repository;

import auth.entity.AuthSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuthSessionRepository extends JpaRepository<AuthSession, UUID> {
    List<AuthSession> findByPhoneNumber(String phoneNumber);

    int countByPhoneNumber(String phoneNumber);

    AuthSession findBySessionId(UUID sessionID);
}