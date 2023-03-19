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
    public Project save(Project entity, String token) {
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

    /**
     * Retrieves a list of {@link ItemWithQuantity} objects for the items associated with the specified project.
     *
     * @param project the project for which to retrieve the list of items with quantities
     * @return a list of {@link ItemWithQuantity} objects, each of which represents an item associated with the project and its quantity
     * @throws NoSuchFieldException if an error occurs while retrieving the items associated with the project
     */
    public List<ItemWithQuantity> getProjectItemsListOfPairs(Project project) throws NoSuchFieldException {

        Map<Long, Integer> items = new HashMap<>();

        // Convert the project's items map from String keys and values to Long and Integer values, and store the result in the items map
        project.generateItemsAsMapOfStrings()
                .forEach((key, value) -> items.put(
                    Long.valueOf(key), Integer.valueOf(value))
                );

        // Create a list of item IDs from the items map
        List<Long> itemIds = new ArrayList<Long>(project.generateItemsAsMapOfStrings().keySet().stream().map(i-> Long.valueOf(i)).collect(Collectors.toList()));
        List<Item> itemsList;
        try {
            itemsList = itemService.findItemsById(itemIds);
        } catch (NoSuchFieldException e) {
            throw e;
        }

        // Create a list of ItemWithQuantity objects, each of which represents an item associated with the project and its quantity
        List<ItemWithQuantity> listOfPairs = new ArrayList<>();
        for(Item item : itemsList){
            listOfPairs.add(new ItemWithQuantity(item, items.get(item.getId())));
        }

        return listOfPairs;
    }

    public Map<Item, Integer> getProjectItemsMap(Project project) throws NoSuchFieldException {

        Map<Long, Integer> items = new HashMap<>();

        project.generateItemsAsMapOfStrings()
                .forEach((key, value) -> items.put(
                        Long.valueOf(key), Integer.valueOf(value))
                );

        List<Long> itemIds = new ArrayList<Long>(project.generateItemsAsMapOfStrings().keySet().stream().map(i-> Long.valueOf(i)).collect(Collectors.toList()));
        List<Item> itemsList;
        try {
            itemsList = itemService.findItemsById(itemIds);
        } catch (NoSuchFieldException e) {
            throw e;
        }
        Map<Item, Integer> itemsMap = new HashMap<>();
        List<ItemWithQuantity> listOfPairs = new ArrayList<>();
        for(Item item : itemsList){
            itemsMap.put(item, items.get(item.getId()));
        }

        return itemsMap;
    }

//    AbstractMap.SimpleEntry<List<Project>, List<ItemWithQuantity>>
    public Object[] getProjectByUserIdAndProjectIdWithItemObjects(Long projectId, String token) throws EntityNotFoundException, JsonProcessingException {
        Optional<Project> userProject = findProjectByUserIdAndProjectId(projectId, token);
        List<ItemWithQuantity> itemsWithQuantities;
        Map<Item,Integer> map;

        if(userProject.isPresent()){
            try {
                itemsWithQuantities = getProjectItemsListOfPairs(userProject.get());
//                map = getProjectItemsMap(userProject.get());

            } catch (NoSuchFieldException e) {
                itemsWithQuantities = new ArrayList<>();
//                map = new HashMap<>();
            }
        } else throw new EntityNotFoundException("There is no such project!");

//        AbstractMap.SimpleEntry<List<Project>, List<ItemWithQuantity>>
        Object[] pair = new Object[] {userProject, itemsWithQuantities};

        return pair;
    }


    private Long tokenIdDecoder(String token){
        String jwt = token.substring(7);
        Long userAccountId = tokenProvider.userIdDecoder(jwt);
        return userAccountId;
    }

}
