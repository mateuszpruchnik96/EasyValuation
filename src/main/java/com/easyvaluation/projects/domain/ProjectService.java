package com.easyvaluation.projects.domain;

import com.easyvaluation.authentication.domain.TokenProvider;
import com.easyvaluation.foundations.domain.AbstractService;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.materialslibrary.domain.item.ItemService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.NestedServletException;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService implements AbstractService<Project> {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectItemsRepository projectItemsRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    TokenProvider tokenProvider;

    /**
     * Saves a new project, associating it with the current user.
     *
     * @param entity the project to be saved
     * @return the saved project entity
     */
    public Project save(Project entity) {
        // TO REWORK
        UserAccount user = userAccountRepository.getById(1L);
        entity.setUserAccount(user);
        entity = projectRepository.save(entity);
        return entity;
    }

    /**
     * Saves a new project, associating it with the user specified by the provided token.
     *
     * @param entity the project to be saved
     * @param token the authentication token for the user
     * @return the saved project entity
     */
    @Transactional
    public Project save(Project entity, String token) throws NestedServletException {
        Long userAccountId = this.tokenIdDecoder(token);
        UserAccount user = userAccountRepository.getById(userAccountId);
        entity.setUserAccount(user);
        System.out.println(entity.toString());
        entity = projectRepository.save(entity);
        return entity;
    }

    /**
     * Finds all projects associated with the user specified by the provided token.
     *
     * @param token the authentication token for the user
     * @return a list of projects associated with the user
     */
    public List<Project> findProjectsByUserId(String token){
        Long userAccountId = this.tokenIdDecoder(token);

        List<Project> userProjects = projectRepository.findByUserAccountId(userAccountId);
        return userProjects;
    }

    /**
     * Finds the project with the specified ID that is associated with the user specified by the provided token.
     *
     * @param projectId the ID of the project to find
     * @param token the authentication token for the user
     * @return an Optional containing the project with the specified ID, if it exists and is associated with the user
     */
    public Optional<Project> findProjectByUserIdAndProjectId(Long projectId, String token){
        Long userAccountId = this.tokenIdDecoder(token);

        Optional<Project> userProject = Optional.ofNullable(projectRepository.findByUserAccountIdAndId(userAccountId, projectId));
        return userProject;
    }


    private Long tokenIdDecoder(String token){
        String jwt = token.substring(7);
        Long userAccountId = tokenProvider.userIdDecoder(jwt);
        return userAccountId;
    }

}
