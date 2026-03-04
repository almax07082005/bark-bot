package ali.barkbot.mapper;

import ali.barkbot.dto.DeliverySelectionDto;
import ali.barkbot.dto.FractionPayloadDto;
import ali.barkbot.dto.QuantityEntryDto;
import ali.barkbot.entity.OrderSessionEntity;
import ali.barkbot.model.OrderStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = OrderStep.class)
public interface OrderSessionMapper {

    @Mapping(target = "step", expression = "java(OrderStep.QUANTITY)")
    void withFractionSelected(@MappingTarget OrderSessionEntity target, FractionPayloadDto payload);

    void withDeliverySelected(@MappingTarget OrderSessionEntity target, DeliverySelectionDto dto);

    @Mapping(target = "step", expression = "java(OrderStep.DELIVERY_METHOD)")
    void withQuantityEntered(@MappingTarget OrderSessionEntity target, QuantityEntryDto dto);

    @Mapping(target = "step", expression = "java(OrderStep.CONFIRMATION)")
    void withAddressEntered(@MappingTarget OrderSessionEntity target, String address);

    void withStep(@MappingTarget OrderSessionEntity target, OrderStep step);
}
