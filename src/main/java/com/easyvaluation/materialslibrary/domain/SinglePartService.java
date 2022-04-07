package com.easyvaluation.materialslibrary.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SinglePartService implements AbstractService<SinglePart> {
    @Autowired
    SinglePartRepository singlePartRepository;

    @Override
    public SinglePart save(SinglePart entity) {
        entity = singlePartRepository.save(entity);
        return entity;
    }
}
