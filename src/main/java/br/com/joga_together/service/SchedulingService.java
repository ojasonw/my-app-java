package br.com.joga_together.service;

import br.com.joga_together.dto.scheduling.SchedulingRequestDto;
import br.com.joga_together.dto.scheduling.SchedulingResponseDto;
import br.com.joga_together.dto.scheduling.SchedulingsByGroupIdResponseDto;
import br.com.joga_together.dto.scheduling.UserResponseToSchedulingDto;
import br.com.joga_together.exception.BusinessException;
import br.com.joga_together.mapper.GroupMapper;
import br.com.joga_together.model.Group;
import br.com.joga_together.model.Scheduling;
import br.com.joga_together.model.User;
import br.com.joga_together.repository.GroupRepository;
import br.com.joga_together.repository.SchedulingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulingService {
    private final SchedulingRepository schedulingRepository;
    private final GroupService groupService;
    private final GroupMapper groupMapper;
    private final GroupRepository groupRepository;
    private final UserService userService;

    public SchedulingService(SchedulingRepository schedulingRepository, GroupService groupService, GroupMapper groupMapper, GroupRepository groupRepository, UserService userService) {
        this.schedulingRepository = schedulingRepository;
        this.groupService = groupService;
        this.groupMapper = groupMapper;
        this.groupRepository = groupRepository;
        this.userService = userService;
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

    public List<SchedulingsByGroupIdResponseDto> findAllSchedulingsByGroupId(UUID idGoup) {
        List<Scheduling> allByGroupId = schedulingRepository.findAllByGroupId(idGoup);
        return allByGroupId.stream().map(
                s -> new SchedulingsByGroupIdResponseDto(
                        s.getId(),
                        s.getName(),
                        s.getDate(),
                        s.getTime(),
                        s.getUsers().stream().map(
                                u -> UserResponseToSchedulingDto.of(u)
                        ).toList()
                )
        ).toList();
    }

    @Transactional
    public void addMemberToScheduling(UUID schedulingId, UUID userId) {
        Scheduling scheduling = schedulingRepository.findById(schedulingId).orElseThrow(
                () -> new EntityNotFoundException("scheduling with id: " + schedulingId + ", not found.")
        );
        boolean alreadyMember = schedulingRepository.existsByIdAndUsersId(schedulingId, userId);
        if(alreadyMember){
            throw new BusinessException("this user already in this scheduling");
        }
        boolean userBelogsToGroup = groupRepository.existsByIdAndUsersId(scheduling.getGroup().getId(), userId);

        if (!userBelogsToGroup) {
            throw new BusinessException("user doesn't belong to group");
        }

        User user = userService.findById(userId);
        scheduling.getUsers().add(user);

    }

}
