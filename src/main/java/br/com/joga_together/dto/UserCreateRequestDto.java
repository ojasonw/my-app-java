package br.com.joga_together.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

public record UserCreateRequestDto(
        @Size(min = 3, max = 15)
        String username,
        @Size(min = 8, max = 14)
        String password,
        @CPF
        String cpf,
        @Email
        String email,
        LocalDate bornDate
) {
}
