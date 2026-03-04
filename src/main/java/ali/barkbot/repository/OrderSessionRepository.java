package ali.barkbot.repository;

import ali.barkbot.entity.OrderSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSessionRepository extends JpaRepository<OrderSessionEntity, Long> {
}
