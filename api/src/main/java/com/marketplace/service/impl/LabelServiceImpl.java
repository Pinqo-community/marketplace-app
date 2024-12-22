package com.marketplace.service.impl;

import com.marketplace.dto.LabelDto;
import com.marketplace.entity.Label;
import com.marketplace.exception.AlreadyExistsException;
import com.marketplace.exception.NotFoundException;
import com.marketplace.repository.LabelRepository;
import com.marketplace.service.LabelService;
import com.marketplace.utils.mapper.LabelMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    @Override
    public LabelDto createLabel(LabelDto labelDto) {
        log.info("Received request to create a new label");
        if (labelRepository.existsByNameIgnoreCase(labelDto.getName()))  {
            throw new AlreadyExistsException("Le nom du label existe déjà");
        }
        log.info("Creating label: {}", labelDto);
        Label newlabel = LabelMapper.INSTANCE.toEntity(labelDto);
        log.info("New label created with id {}", newlabel.getId());
        Label savedLabel = labelRepository.save(newlabel);
        return LabelMapper.INSTANCE.toDto(savedLabel);
    }

    @Override
    public LabelDto updateLabel(LabelDto labelDto, Long id) {
        log.info("Received request to update label with id {}", id);
        Label updatedLabel = labelRepository.findById(id)
                .map(existingLabel -> {
                    log.info("Label with id {} found", id);
                    existingLabel.setName(labelDto.getName());
                    existingLabel.setDescription(labelDto.getDescription());
                    return labelRepository.save(existingLabel);
                })
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));
        log.info("Label with id {} updated", id);
        return LabelMapper.INSTANCE.toDto(updatedLabel);

    }


    @Override
    public List<LabelDto> getAllLabels() {
        log.info("Received request to get all labels");
        List<Label> labels = labelRepository.findAll();
        log.info("Returning labels");
        return LabelMapper.INSTANCE.toDtoList(labels);
    }

    @Override
    public LabelDto getLabelById(long id) {
        log.info("Received request to get label with id {}", id);
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));

        return LabelMapper.INSTANCE.toDto(label);
    }

    @Override
    public void deleteLabel(long id) {
        log.info("Received request to delete label with id {}", id);
        labelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Il n'existe pas de label avec cet id"));
        labelRepository.deleteById(id);
        log.info("Label with id {} deleted sucessfully", id);
    }
}
