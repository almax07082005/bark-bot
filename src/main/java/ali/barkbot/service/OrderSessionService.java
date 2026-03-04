package ali.barkbot.service;

import ali.barkbot.entity.OrderSessionEntity;
import ali.barkbot.model.OrderStep;
import ali.barkbot.repository.OrderSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderSessionService {

    private final OrderSessionRepository repository;

    public void start(Long chatId) {
        OrderSessionEntity entity = OrderSessionEntity.builder()
                .chatId(chatId)
                .step(OrderStep.FRACTION)
                .build();
        repository.save(entity);
    }

    public OrderSessionEntity get(Long chatId) {
        return repository.findById(chatId).orElse(null);
    }

    public void update(OrderSessionEntity session) {
        repository.save(session);
    }

    public void remove(Long chatId) {
        repository.deleteById(chatId);
    }

    public boolean hasActiveSession(Long chatId) {
        return repository.existsById(chatId);
    }
}
