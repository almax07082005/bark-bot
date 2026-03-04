package ali.barkbot.entity;

import ali.barkbot.model.OrderStep;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_sessions")
@Entity
public class OrderSessionEntity {

    @Id
    @Column(name = "chat_id", nullable = false, unique = true, updatable = false)
    private Long chatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "step", nullable = false, length = 50)
    private OrderStep step;

    @Column(name = "frac", length = 50)
    private String frac;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Integer price;

    @Column(name = "qty_user")
    private Integer qtyUser;

    @Column(name = "qty_final")
    private Integer qtyFinal;

    @Column(name = "method")
    private String method;

    @Column(name = "address", length = 500)
    private String address;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
