package br.com.joga_together.dto.user;

public record ConfirmCodeDto(
        String email,
        String code
) {
}
