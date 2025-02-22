package com.marketplace.web.controller;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.api.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for label management operations.
 */
@Tag(name = "Labels", description = "Label management in the marketplace")
@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
@Slf4j
public class LabelController {

    private final LabelService labelService;

    /**
     * Creates a new label.
     *
     * @param labelDto label details
     * @return ResponseEntity with created label
     */
    @PostMapping
    @Operation(summary = "Create a label", description = "Add a new label in the marketplace")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created label"),
            @ApiResponse(responseCode = "409", description = "Label with this name already exists")
    })
    public ResponseEntity<LabelDto> createLabel(@Valid @RequestBody LabelDto labelDto) {
        try {
            log.atInfo().log("POST /labels - START: Creating a new label");
            LabelDto savedLabelDto = labelService.createLabel(labelDto);
            log.atInfo().log("POST /labels - label created successfully");
            return new ResponseEntity<>(savedLabelDto, HttpStatus.CREATED);
        } finally {
            log.atInfo().log("POST /label - END: new label created");
        }
    }

    /**
     * Updates label by ID.
     *
     * @param id label identifier
     * @param labelDto updated label details
     * @return ResponseEntity with updated label
     */
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
            log.atInfo().log("PUT /labels/{} - START: Updating label", id);
            LabelDto updatedLabel = labelService.updateLabel(labelDto, id);
            return ResponseEntity.ok(updatedLabel);
        } finally {
            log.atInfo().log("PUT /labels/{} - END: Label updated successfully", id);
        }
    }

    /**
     * Retrieves all labels.
     *
     * @return ResponseEntity with list of all labels
     */
    @GetMapping
    @Operation(summary = "Get all labels", description = "Retrieves all labels in the marketplace")
    @ApiResponse(responseCode = "200", description = "Labels successfully retrieved")
    public ResponseEntity<List<LabelDto>> getAllLabels() {
        try {
            log.atInfo().log("GET /labels - START: Retrieving labels");
            List<LabelDto> labels = labelService.getAllLabels();
            log.atInfo().log("GET /labels - Labels retrieved successfully");
            return ResponseEntity.ok(labels);
        } finally {
            log.atInfo().log("GET /labels - DONE");
        }
    }

    /**
     * Retrieves label by ID.
     *
     * @param id label identifier
     * @return ResponseEntity with found label
     */
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
            log.atInfo().log("GET /labels/{} - START: Retrieving label", id);
            LabelDto label = labelService.getLabelById(id);
            log.atInfo().log("GET /labels/{} - Label retrieved successfully", id);
            return ResponseEntity.ok(label);
        } finally {
            log.atInfo().log("GET /labels/{} - DONE", id);
        }
    }

    /**
     * Deletes label by ID.
     *
     * @param id label identifier
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a label", description = "Deletes a label based on its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Label successfully deleted"),
            @ApiResponse(responseCode = "404", description = "No label found")
    })
    public ResponseEntity<HttpStatus> deleteLabel(@Parameter(description = "Unique label identifier", required = true) @PathVariable Long id) {
        try {
            log.atInfo().log("DELETE /labels/{} - START: Deleting label", id);
            labelService.deleteLabel(id);
            log.atInfo().log("DELETE /labels/{} - Label deleted successfully", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } finally {
            log.atInfo().log("DELETE /labels/{} - END", id);
        }
    }
}
