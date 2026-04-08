package br.com.joga_together.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserCreateRequestDto(
        @Min(3)
        @Max(15)
        String username,
        @Min(8)
        @Max(14)
        @NotBlank
        String password,
        @CPF
        @NotBlank
        String cpf,
        @Email
        @NotBlank
        String email,
        LocalDate bornDate
) {
}
