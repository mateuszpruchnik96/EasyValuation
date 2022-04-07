package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwoDimensionalService implements AbstractService<TwoDimensional> {
    @Autowired
    TwoDimensionalRepository twoDimensionalRepository;

    @Override
    public TwoDimensional save(TwoDimensional entity) {
        entity = twoDimensionalRepository.save(entity);
        return entity;
    }
}
