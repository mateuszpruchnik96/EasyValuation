package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.AbstractFactory;

public class ProjectFactory implements AbstractFactory<Project> {

    @Override
    public Project create() {
        return new Project();
    }
}
