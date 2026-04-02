package br.com.joga_together.service;

import br.com.joga_together.dto.group.CreateGroupRequestDto;
import br.com.joga_together.exception.UserAlreadyInGroupException;
import br.com.joga_together.mapper.GroupMapper;
import br.com.joga_together.model.Group;
import br.com.joga_together.model.User;
import br.com.joga_together.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, UserService userService, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;

        this.userService = userService;
        this.groupMapper = groupMapper;
    }

    @Transactional
    public UUID createGroup(CreateGroupRequestDto request) {
        Group group = groupMapper.dtoToEntity(request);
        User byId = userService.findById(request.masterId());
        group.setMasterGroup(byId.getId());
        group.getUsers().add(byId);
        Group save = groupRepository.save(group);
        return save.getId();
    }

    @Transactional
    public void addUserToGroup(UUID groupId, UUID userId) {
        User user = userService.findById(userId);
        Group group = findGroupById(groupId);
        boolean exists = groupRepository.existsByIdAndUsersId(groupId, userId);
        if(exists)throw new UserAlreadyInGroupException("User already in group");
        group.getUsers().add(user);
        try{
            groupRepository.save(group);
        } catch (Exception e) {
            throw new RuntimeException("error to add user to group");
        }
    }

    public Group findGroupById(UUID uuid) {
        return groupRepository.findById(uuid).orElseThrow(
                () -> new NoSuchElementException("group with this id: " + uuid + ", not found")
        );
    }
}
