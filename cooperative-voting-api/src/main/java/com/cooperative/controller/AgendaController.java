package com.cooperative.controller;

import com.cooperative.dto.AgendaRequestDto;
import com.cooperative.dto.AgendaResponseDto;
import com.cooperative.dto.AgendaSessionDto;
import com.cooperative.dto.OpenAgendaRequestDto;
import com.cooperative.exception.ExceptionsDetails;
import com.cooperative.service.inter.AgendaServiceI;
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
@RequestMapping("/agendas")
@RequiredArgsConstructor
@Tag(name = "Agendas", description = "Endpoints for managing guidelines")
public class AgendaController {

    private final AgendaServiceI agendaServiceI;

    @Operation(
            summary = "Create a new agenda",
            description = "Creates a new agenda with a unique title."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "201",
                    description = "Agenda created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendaResponseDto.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 400,
                                  "message": "Title is required",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Agenda already exists",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 409,
                                  "message": "An agenda with the title already exists: Budget Approval",
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
    @PostMapping
    public ResponseEntity<AgendaResponseDto> createAgenda(@RequestBody @Valid AgendaRequestDto agendaRequestDto) {

        return new ResponseEntity<>(agendaServiceI.createAgenda(agendaRequestDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Open voting for an agenda",
            description = "Opens a voting session for an agenda if it is not already open or closed."
    )
    @ApiResponses(value = {

            @ApiResponse(
                    responseCode = "200",
                    description = "Voting session opened successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AgendaSessionDto.class)
                    )
            ),

            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "statusCode": 400,
                                  "message": "Duration must be greater than zero",
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
                                  "message": "Agenda not found for id: 12",
                                  "timestamp": "2025-12-16T10:30:00"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Voting session conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionsDetails.class),
                            examples = {

                                    @ExampleObject(
                                            name = "Voting session already open",
                                            summary = "Agenda already open",
                                            value = """
                                        {
                                          "statusCode": 409,
                                          "message": "The voting session is already open.",
                                          "timestamp": "2025-12-16T10:30:00"
                                        }
                                        """
                                    ),

                                    @ExampleObject(
                                            name = "Voting session already closed",
                                            summary = "Agenda already closed",
                                            value = """
                                        {
                                          "statusCode": 409,
                                          "message": "The voting session has already been closed and cannot be reopened.",
                                          "timestamp": "2025-12-16T10:30:00"
                                        }
                                        """
                                    )
                            }
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
    @PatchMapping("/{agendaId}/open")
    public ResponseEntity<AgendaSessionDto> openAgenda(@PathVariable long agendaId,
                                                       @RequestBody @Valid OpenAgendaRequestDto openAgendaRequestDto) {

        return new ResponseEntity<>(agendaServiceI.openAgenda(agendaId, openAgendaRequestDto.getDurationMinutes()), HttpStatus.OK);
    }
}
