package br.com.joga_together.controller;

import br.com.joga_together.dto.group.CreateGroupRequestDto;
import br.com.joga_together.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.created(URI.create("/groups/" + groupId)).body(groupId);
    }

    @PatchMapping("/{groupId}/add-user/{userId}")
    //TODO trocar o void por um retorno mais adequado, como um DTO de resposta ou um status HTTP específico
    public ResponseEntity<Void>addUserToGroup(@PathVariable("groupId")UUID groupId, @PathVariable("userId")UUID userId){
        //TODO implementar a lógica para adicionar um usuário a um grupo usando o GroupService
        groupService.addUserToGroup(groupId, userId);
        return ResponseEntity.ok().build();
    }
}
