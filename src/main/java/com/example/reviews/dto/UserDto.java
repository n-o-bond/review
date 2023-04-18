package com.example.reviews.dto;

import com.example.reviews.model.User;

public record UserDto(long id, String firstName, String lastName, String email) {
    public static UserDto entityToDto(User user){
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public static User dtoTOEntity(UserDto userDto){
        return User.builder()
                .id(userDto.id)
                .firstName(userDto.firstName)
                .lastName(userDto.lastName)
                .email(userDto.email)
                .build();
    }
}
