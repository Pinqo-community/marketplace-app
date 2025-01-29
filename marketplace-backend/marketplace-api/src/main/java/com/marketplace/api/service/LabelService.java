package com.marketplace.api.service;


import com.marketplace.api.dto.label.LabelDto;

import java.util.List;

/**
 * Service interface for managing label operations.
 */
public interface LabelService {
    /**
     * Updates an existing label.
     *
     * @param labelDto DTO containing label update data
     * @param id unique identifier of the label
     * @return updated LabelDto
     */
    LabelDto updateLabel(LabelDto labelDto, Long id);

    /**
     * Retrieves all labels.
     *
     * @return List of all labels as LabelDto objects
     */
    List<LabelDto> getAllLabels();

    /**
     * Retrieves a label by its ID.
     *
     * @param id unique identifier of the label
     * @return LabelDto of the found label
     */
    LabelDto getLabelById(long id);

    /**
     * Creates a new label.
     *
     * @param labelDto DTO containing label creation data
     * @return created LabelDto
     */
    LabelDto createLabel(LabelDto labelDto);

    /**
     * Deletes a label by its ID.
     *
     * @param id unique identifier of the label
     */
    void deleteLabel(long id);
}
