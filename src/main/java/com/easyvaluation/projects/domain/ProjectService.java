package com.easyvaluation.projects.domain;

import com.easyvaluation.foundations.domain.AbstractService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService implements AbstractService<Project> {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public Project save(Project entity) {
        // TO REWORK
        UserAccount user = userAccountRepository.getById(1L);
        entity.setUserAccount(user);
        entity = projectRepository.save(entity);
        return entity;
    }

    public List<Project> findProjectsByUserId(Long userAccountId){
        List<Project> userProjects = projectRepository.findByUserAccountId(userAccountId);
        return userProjects;
    }

}
