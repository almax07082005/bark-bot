package ali.barkbot.model;

import lombok.Builder;

@Builder
public record CommandString(
        String command,
        CameFrom cameFrom
) {
}
