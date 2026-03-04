package ali.barkbot.dto;

import ali.barkbot.model.OrderStep;

/**
 * Selection of delivery method for order session mapping.
 */
public record DeliverySelectionDto(OrderStep step, String method, String address) {
}
