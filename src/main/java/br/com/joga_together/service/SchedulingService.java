package br.com.joga_together.service;

import br.com.joga_together.dto.scheduling.SchedulingRequestDto;
import br.com.joga_together.dto.scheduling.SchedulingResponseDto;
import br.com.joga_together.dto.scheduling.UserResponseToSchedulingDto;
import br.com.joga_together.mapper.GroupMapper;
import br.com.joga_together.model.Group;
import br.com.joga_together.model.Scheduling;
import br.com.joga_together.model.User;
import br.com.joga_together.repository.SchedulingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulingService {
    private final SchedulingRepository schedulingRepository;
    private final GroupService groupService;
    private final GroupMapper groupMapper;

    public SchedulingService(SchedulingRepository schedulingRepository, GroupService groupService, GroupMapper groupMapper) {
        this.schedulingRepository = schedulingRepository;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
    }

    @Transactional
    public UUID createScheduling(SchedulingRequestDto dto) {
        Group groupById = groupService.findGroupById(dto.groupId());
        Set<UUID> idsPermitidos = new HashSet<>(dto.usersId());
        Set<User> users = groupById.getUsers()
                .stream()
                .filter(u -> idsPermitidos.contains(u.getId())
                ).collect(Collectors.toSet());
        Scheduling scheduling = new Scheduling();
        scheduling.setName(dto.name());
        scheduling.setTime(dto.time());
        scheduling.setDate(dto.date());
        scheduling.setUsers(users);
        scheduling.setGroup(groupById);
        var schedulingFinish = schedulingRepository.save(scheduling);
        return schedulingFinish.getId();
    }

    public List<SchedulingResponseDto> findAllSchedulings() {
        List<Scheduling> all = schedulingRepository.findAll();
        return all.stream()
                .map(scheduling -> new SchedulingResponseDto(
                        scheduling.getId(),
                        scheduling.getName(),
                        scheduling.getDate(),
                        scheduling.getTime(),
                        scheduling.getUsers().stream()
                                .map(u -> new UserResponseToSchedulingDto(
                                        u.getUsername(), u.getEmail()
                                ))
                                .toList(),
                        groupMapper.entityToGroupSchedulingDto(scheduling.getGroup())
                ))
                .collect(Collectors.toList());
    }
}
