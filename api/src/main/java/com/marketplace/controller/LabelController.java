package com.marketplace.controller;

import com.marketplace.dto.Label.LabelDto;
import com.marketplace.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labels")
@RequiredArgsConstructor
@Slf4j
@Validated
public class LabelController {

    private final LabelService labelService;

    @PostMapping
    public ResponseEntity<?> createLabel(@Valid @RequestBody LabelDto labelDto) {
        return ResponseEntity.ok(labelService.createLabel(labelDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabelDto> updateLabel(@PathVariable Long id, @Valid @RequestBody LabelDto labelDto) {
        return ResponseEntity.ok(labelService.updateLabel(labelDto, id));
    }




}
