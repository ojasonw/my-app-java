package br.com.joga_together.dto.scheduling;

import br.com.joga_together.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record SchedulingRequestDto(
        UUID groupId,
        String name,
        LocalDate date,
        LocalTime time,
        List<UUID>usersId
) {
}
