package br.com.joga_together.controller;

import br.com.joga_together.dto.scheduling.SchedulingRequestDto;
import br.com.joga_together.dto.scheduling.SchedulingResponseDto;
import br.com.joga_together.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scheduling")
public class SchedulingController {
    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @PostMapping("/create")
    public ResponseEntity<UUID>createScheduling(@RequestBody SchedulingRequestDto dto){
        var scheduling = schedulingService.createScheduling(dto);
        return ResponseEntity.status(201).body(scheduling);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SchedulingResponseDto>>getAll(){
        return ResponseEntity.ok(schedulingService.findAllSchedulings());
    }
}
