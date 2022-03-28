package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService implements AbstractService<Project> {

    @Autowired
    ProjectRepository projectRepository;

    @Override
    public Project save(Project entity) {
        entity = projectRepository.save(entity);
        return entity;
    }



}
