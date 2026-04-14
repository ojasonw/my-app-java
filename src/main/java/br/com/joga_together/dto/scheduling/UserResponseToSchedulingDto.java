package br.com.joga_together.dto.scheduling;

import br.com.joga_together.model.User;

public record UserResponseToSchedulingDto(
        String username,
        String email
) {

    public static UserResponseToSchedulingDto of(User user){
        return new UserResponseToSchedulingDto(
                user.getUsername(),
                user.getEmail()
        );
    }
}
