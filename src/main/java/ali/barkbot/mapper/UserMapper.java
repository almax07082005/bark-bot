package ali.barkbot.mapper;

import ali.barkbot.entity.UserEntity;
import ali.barkbot.entity.model.CameFrom;
import com.pengrad.telegrambot.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "pid", expression = "java(user.id())")
    @Mapping(target = "username", expression = "java(user.username())")
    @Mapping(target = "firstName", expression = "java(user.firstName())")
    @Mapping(target = "lastName", expression = "java(user.lastName())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "accessedAt", ignore = true)
    UserEntity toEntity(User user, CameFrom cameFrom);
}
