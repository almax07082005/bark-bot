package ali.barkbot.repository;

import ali.barkbot.entity.OrderCallbackButtonEntity;
import ali.barkbot.model.CallbackButtonType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderCallbackButtonRepository extends JpaRepository<OrderCallbackButtonEntity, String> {

    List<OrderCallbackButtonEntity> findByButtonTypeOrderById(CallbackButtonType buttonType);
}
