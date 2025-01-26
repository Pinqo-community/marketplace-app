package com.marketplace.api.service;


import com.marketplace.api.dto.label.LabelDto;

import java.util.List;

public interface LabelService {
    LabelDto updateLabel(LabelDto labelDto, Long id);
    List<LabelDto> getAllLabels();
    LabelDto getLabelById(long id);
    LabelDto createLabel(LabelDto labelDto);
    void deleteLabel(long id);
}
