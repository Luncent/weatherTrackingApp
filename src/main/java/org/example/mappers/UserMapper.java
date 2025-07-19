package org.example.mappers;

import org.example.dto.UserDTO;
import org.example.entities.HttpSession;
import org.example.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", expression = "java(user.getId())")
    @Mapping(target = "login", expression = "java(user.getLogin())")
    @Mapping(target = "sessionId", expression = "java(Optional.ofNullable(session.getId()))")
    UserDTO userToUserDTO(User user, HttpSession session);
}
