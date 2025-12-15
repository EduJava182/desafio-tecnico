package com.cooperative.controller;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.service.inter.VoteServiceI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteServiceI voteServiceI;

    @PostMapping("/{agendaId}/votes")
    public ResponseEntity<Void> submitVote(@PathVariable long agendaId,
                                           @RequestBody @Valid VoteRequestDto voteRequestDto) {

        voteServiceI.submitVote(agendaId, voteRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{agendaId}/result")
    public ResponseEntity<VoteResponseDto> voteCounter(@PathVariable long agendaId) {
        return new ResponseEntity<>(voteServiceI.voteCounter(agendaId), HttpStatus.OK);
    }
}
