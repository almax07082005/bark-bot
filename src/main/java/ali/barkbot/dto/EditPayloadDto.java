package ali.barkbot.dto;

import ali.barkbot.model.OrderStep;

import java.util.Map;

/**
 * Payload for EDIT callback buttons (target: OrderStep).
 */
public record EditPayloadDto(OrderStep target) {

    public static EditPayloadDto fromPayload(Map<String, String> payload) {
        return new EditPayloadDto(OrderStep.valueOf(payload.getOrDefault("target", "")));
    }
}
