package ali.barkbot.dto;

import java.util.Map;

/**
 * Payload for DELIVERY callback buttons (method label).
 */
public record DeliveryPayloadDto(String method) {

    public static DeliveryPayloadDto fromPayload(Map<String, String> payload) {
        return new DeliveryPayloadDto(payload.getOrDefault("method", ""));
    }
}
