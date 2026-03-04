package ali.barkbot.entity;

import ali.barkbot.model.CallbackButtonType;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_callback_buttons")
@Entity
public class OrderCallbackButtonEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, unique = true, length = 64)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "button_type", nullable = false, length = 32)
    private CallbackButtonType buttonType;

    @Column(name = "label", nullable = false)
    private String label;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", columnDefinition = "jsonb", nullable = false)
    private Map<String, String> payloadJson;
}
