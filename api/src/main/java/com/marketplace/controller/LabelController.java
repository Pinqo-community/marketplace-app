package com.marketplace.controller;

import com.marketplace.dto.Label.LabelDto;
import com.marketplace.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Labels", description = "Label management in the marketplace")
@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
@Slf4j
@Validated
@Getter
public class LabelController {

    private final LabelService labelService;

    @PostMapping
    @Operation(summary = "Create a label", description = "Add a new label in the marketplace")
    @ApiResponse(responseCode = "201", description = "Successfully created label")
    public ResponseEntity<LabelDto> createLabel(@Valid @RequestBody LabelDto labelDto) {
        try {
            log.info("POST /labels - START: Creating a new label");
            LabelDto savedLabelDto = labelService.createLabel(labelDto);
            log.info("POST /labels - label created successfully");
            return new ResponseEntity<>(savedLabelDto, HttpStatus.CREATED);
        } finally {
            log.info("POST /label - END: new label created");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a label", description = "The ID in the URL is mandatory and overrides any ID provided in the request body.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Label successfully updated."),
            @ApiResponse(responseCode = "404", description = "No label found.")
    })
    public ResponseEntity<LabelDto> updateLabel(@Parameter(description = "Unique label identifier", required = true)
                                               @PathVariable("id") Long id,
                                               @Valid @RequestBody LabelDto labelDto) {
        try {
            log.info("PUT /labels/{} - START:  Updating label", id);
            LabelDto updatedLabel = labelService.updateLabel(labelDto, id);
            return ResponseEntity.ok(updatedLabel);
        } finally {
            log.info("PUT /labels/{} - END: Label updated successfully", id);
        }
    }

    @GetMapping
    @Operation(summary = "Get all labels", description = "Retrieves all labels in the marketplace")
    @ApiResponse(responseCode = "200", description = "Labels successfully retrieved")
    public ResponseEntity<List<LabelDto>> getAllLabels() {
        try {
            log.info("GET /labels - START: Retrieving labels");
            List<LabelDto> labels = labelService.getAllLabels();
            log.info("GET /labels - Labels retrieved successfully");
            return ResponseEntity.ok(labels);
        } finally {
            log.info("GET /labels - DONE");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a label by ID", description = "Retrieves a label based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Label successfully recovered."),
            @ApiResponse(responseCode = "404", description = "No label found."),
            @ApiResponse(responseCode = "410", description = "Label is no longer available."),
            @ApiResponse(responseCode = "500", description = "Internal server error.")
    })
    public ResponseEntity<LabelDto> getLabelById(@Parameter(description = "Unique label identifier", required = true) @PathVariable Long id) {
        try {
            log.info("GET /labels/{} - START: Retrieving label", id);
            LabelDto label = labelService.getLabelById(id);
            log.info("GET /labels/{} - Label retrieved successfully", id);
            return ResponseEntity.ok(label);
        } finally {
            log.info("GET /labels/{} - DONE", id);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a label", description = "Deletes a label based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Label successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No label found")
    })
    public ResponseEntity<HttpStatus> deleteLabel( @Parameter(description = "Unique product identifier", required = true)@PathVariable Long id) {
        try {
            log.info("DELETE /labels/{} - Deleting label", id);
            log.info("Controller: Attempting to delete label with ID {}", id);
            labelService.deleteLabel(id);
            log.info("DELETE /labels/{} - Label deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }finally {
            log.info("DELETE /labels/{} - DONE", id);
        }
    }
}
