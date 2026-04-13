package br.com.joga_together.dto.scheduling;

import br.com.joga_together.dto.user.GroupResponseToSchedulingDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SchedulingsByGroupIdResponseDto(
        UUID id,
        String name,
        LocalDate date,
        LocalTime time,
        List<UserResponseToSchedulingDto>users
) {
}
