package br.com.joga_together.service;

import br.com.joga_together.dto.CreateGroupRequestDto;
import br.com.joga_together.mapper.GroupMapper;
import br.com.joga_together.model.Group;
import br.com.joga_together.model.User;
import br.com.joga_together.repository.GroupRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public UUID createGroup(CreateGroupRequestDto request){
        Group group = groupMapper.dtoToEntity(request);
        User byId = userService.findById(request.masterId());
        group.setMasterGroup(byId.getId());
        group.getUsers().add(byId);
        Group save = groupRepository.save(group);
        return save.getId();
    }
}
