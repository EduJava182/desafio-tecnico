package com.cooperative.controller;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.service.inter.AgendaServiceI;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agendas")
@RequiredArgsConstructor
public class AgendaController {

    private final AgendaServiceI agendaServiceI;

    @PostMapping
    public ResponseEntity<AgendaResponseDto> createAgenda(@RequestBody @Valid AgendaRequestDto agendaRequestDto) {

        return new ResponseEntity<>(agendaServiceI.createAgenda(agendaRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{agendaId}/open")
    public ResponseEntity<AgendaSessionDto> openAgenda(@PathVariable long agendaId,
                                                       @Valid @RequestBody OpenAgendaRequestDto openAgendaRequestDto) {

        return new ResponseEntity<>(agendaServiceI.openAgenda(agendaId, openAgendaRequestDto.getDurationMinutes()), HttpStatus.OK);
    }
}
