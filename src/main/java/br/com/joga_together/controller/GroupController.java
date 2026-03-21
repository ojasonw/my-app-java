package br.com.joga_together.controller;

import br.com.joga_together.dto.CreateGroupRequestDto;
import br.com.joga_together.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/groups")
public class GroupController {
    public static final String VERSION = "/v1";
    public static final String POST_CREATE_GROUP = "/create";

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(value = POST_CREATE_GROUP, produces = "application/json")
    public ResponseEntity<UUID>postCreateGroup(@RequestBody CreateGroupRequestDto dto){
        UUID groupId = groupService.createGroup(dto);
        return ResponseEntity.created(URI.create("/groups/" + groupId)).body(UUID.randomUUID());
    }
}
