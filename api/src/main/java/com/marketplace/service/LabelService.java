package com.marketplace.service;

import com.marketplace.dto.LabelDto;
import java.util.List;

public interface LabelService {
    LabelDto updateLabel(LabelDto labelDto, Long id);
    List<LabelDto> getAllLabels();
    LabelDto getLabelById(long id);
    LabelDto createLabel(LabelDto labelDto);
    void deleteLabel(long id);
}
