package ali.barkbot.mapper;

import ali.barkbot.entity.UserEntity;
import ali.barkbot.model.CameFrom;
import com.pengrad.telegrambot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "pid", expression = "java(user.id())")
    @Mapping(target = "username", expression = "java(user.username())")
    @Mapping(target = "firstName", expression = "java(user.firstName())")
    @Mapping(target = "lastName", expression = "java(user.lastName())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accessedAt", ignore = true)
    UserEntity toEntity(User user, CameFrom cameFrom);

    @Mapping(target = "pid", ignore = true)
    @Mapping(target = "cameFrom", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accessedAt", ignore = true)
    void updateEntity(@MappingTarget UserEntity existingUser, UserEntity user);
}
