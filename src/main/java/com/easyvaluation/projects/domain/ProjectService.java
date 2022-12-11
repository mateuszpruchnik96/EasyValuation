package com.easyvaluation.projects.domain;

import com.easyvaluation.authentication.domain.TokenProvider;
import com.easyvaluation.foundations.domain.AbstractService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService implements AbstractService<Project> {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    TokenProvider tokenProvider;

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

    public Optional<Project> findProjectByUserIdAndProjectId(Long projectId, String token){
        String jwt = token.substring(7);

        Long userAccountId = tokenProvider.userIdDecoder(jwt);

        Optional<Project> userProject = Optional.ofNullable(projectRepository.findByUserAccountIdAndId(userAccountId, projectId));
        return userProject;
    }

}
