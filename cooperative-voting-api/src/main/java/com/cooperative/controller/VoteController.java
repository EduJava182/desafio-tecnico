package com.cooperative.controller;

import com.cooperative.dto.VoteRequestDto;
import com.cooperative.dto.VoteResponseDto;
import com.cooperative.exception.ExceptionsDetails;
import com.cooperative.service.inter.VoteServiceI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
@Tag(name = "Votes", description = "Endpoints for managing votes")
public class VoteController {

    private final VoteServiceI voteServiceI;

    @Operation(
            summary = "Submit a vote for an agenda",
            description = "Registers a user's vote for an agenda while the voting session is open."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Vote submitted successfully"
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid vote request or voting session not open",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 400,
                                  "message": "Voting session is not open",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Agenda not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 404,
                                  "message": "Agenda not found for id: 5",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "User has already voted on this agenda",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 409,
                                  "message": "User already voted for this agenda",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 500,
                                  "message": "Unexpected internal error",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            )
    })
    @PostMapping("/{agendaId}/votes")
    public ResponseEntity<Void> submitVote(@PathVariable long agendaId,
                                           @RequestBody @Valid VoteRequestDto voteRequestDto) {

        voteServiceI.submitVote(agendaId, voteRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get voting results for an agenda",
            description = "Returns the voting result after the voting session has been closed."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Vote results returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VoteResponseDto.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Voting session is still open",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 400,
                                  "message": "Voting session is still open",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Agenda not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 404,
                                  "message": "Agenda not found for id: 7",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 500,
                                  "message": "Unexpected internal error",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            )
    })
    @GetMapping("/{agendaId}/result")
    public ResponseEntity<VoteResponseDto> voteCounter(@PathVariable long agendaId) {

        return new ResponseEntity<>(voteServiceI.voteCounter(agendaId), HttpStatus.OK);
    }
}
