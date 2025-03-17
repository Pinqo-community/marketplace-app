package com.marketplace.core.service.impl;

import com.marketplace.api.dto.label.LabelDto;
import com.marketplace.api.exception.AlreadyExistsException;
import com.marketplace.api.exception.NotFoundException;
import com.marketplace.api.service.LabelService;
import com.marketplace.core.entity.Label;
import com.marketplace.core.repository.LabelRepository;
import com.marketplace.core.utils.mapper.LabelMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of LabelService interface for managing label operations.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    /**
     * Creates a new label.
     *
     * @param labelDto label data
     * @return created label DTO
     * @throws AlreadyExistsException if label name already exists
     */
    @Override
    public LabelDto createLabel(LabelDto labelDto) {
        log.atInfo().log("Creating new label: {}", labelDto);

        if (labelRepository.existsByNameIgnoreCase(labelDto.getName())) {
            throw new AlreadyExistsException("Le nom du label existe déjà");
        }

        Label newlabel = labelMapper.toEntity(labelDto);
        Label savedLabel = labelRepository.save(newlabel);

        log.atInfo().log("Label created with id: {}", savedLabel.getId());
        return labelMapper.toDto(savedLabel);
    }

    /**
     * Updates an existing label.
     *
     * @param labelDto updated label data
     * @param id label identifier
     * @return updated label DTO
     * @throws NotFoundException if label not found
     */
    @Override
    public LabelDto updateLabel(LabelDto labelDto, Long id) {
        log.atInfo().log("Updating label with id: {}", id);

        Label updatedLabel = labelRepository.findById(id)
                .map(existingLabel -> {
                    existingLabel.setName(labelDto.getName());
                    existingLabel.setDescription(labelDto.getDescription());
                    return labelRepository.save(existingLabel);
                })
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));

        log.atInfo().log("Label updated successfully");
        return labelMapper.toDto(updatedLabel);
    }

    /**
     * Retrieves all labels.
     *
     * @return list of label DTOs
     */
    @Override
    public List<LabelDto> getAllLabels() {
        log.atInfo().log("Retrieving all labels");
        List<Label> labels = labelRepository.findAll();
        return labelMapper.toDtoList(labels);
    }

    /**
     * Retrieves label by ID.
     *
     * @param id label identifier
     * @return label DTO
     * @throws NotFoundException if label not found
     */
    @Override
    public LabelDto getLabelById(Long id) {
        log.atInfo().log("Retrieving label with id: {}", id);

        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));

        return labelMapper.toDto(label);
    }

    /**
     * Deletes a label.
     *
     * @param id label identifier
     * @throws NotFoundException if label not found
     */
    @Override
    public void deleteLabel(long id) {
        log.atInfo().log("Deleting label with id: {}", id);

        labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));

        labelRepository.deleteById(id);
        log.atInfo().log("Label deleted successfully");
    }
}
