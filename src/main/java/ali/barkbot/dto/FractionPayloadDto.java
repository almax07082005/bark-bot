package ali.barkbot.dto;

import java.util.Map;

/**
 * Payload for FRACTION callback buttons (frac, name, price).
 */
public record FractionPayloadDto(String frac, String name, int price) {

    public static FractionPayloadDto fromPayload(Map<String, String> payload) {
        String frac = payload.getOrDefault("frac", "");
        String name = payload.getOrDefault("name", "");
        int price = Integer.parseInt(payload.getOrDefault("price", "0"));
        return new FractionPayloadDto(frac, name, price);
    }
}
