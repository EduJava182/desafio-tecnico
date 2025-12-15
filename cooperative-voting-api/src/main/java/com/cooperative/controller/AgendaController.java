package com.cooperative.controller;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionDto;
import com.cooperative.service.inter.AgendaServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaServiceI agendaServiceI;

    @PostMapping
    public ResponseEntity<AgendaResponseDto> createAgenda(@RequestBody @Valid AgendaRequestDto agendaRequestDto) {
        return new ResponseEntity<>(agendaServiceI.createAgenda(agendaRequestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AgendaResponseDto>> getAllAgendas() {
        return new ResponseEntity<>(agendaServiceI.getAllAgendas(), HttpStatus.OK);
    }

    @PostMapping("/{agendaId}/open")
    public ResponseEntity<AgendaSessionDto> openAgenda(@PathVariable long agendaId,
                                                        @RequestParam(defaultValue = "1") long duration) {
        return new ResponseEntity<>(agendaServiceI.openAgenda(agendaId, duration), HttpStatus.OK);
    }
}
