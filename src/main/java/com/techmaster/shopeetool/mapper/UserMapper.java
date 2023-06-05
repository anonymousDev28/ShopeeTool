package com.techmaster.shopeetool.mapper;


import com.techmaster.shopeetool.dto.UserDto;
import com.techmaster.shopeetool.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRoleSet()
//                user.getPassword()
        );
        return userDto;
    }
}
