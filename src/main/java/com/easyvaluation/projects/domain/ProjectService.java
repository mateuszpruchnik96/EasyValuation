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

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService implements AbstractService<Project> {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    TokenProvider tokenProvider;

    public Project save(Project entity) {
        // TO REWORK
        UserAccount user = userAccountRepository.getById(1L);
        entity.setUserAccount(user);
        entity = projectRepository.save(entity);
        return entity;
    }

     public Project save(Project entity, String token) {
        Long userAccountId = this.tokenIdDecoder(token);
        UserAccount user = userAccountRepository.getById(userAccountId);
        entity.setUserAccount(user);
         System.out.println(entity.toString());
        entity = projectRepository.save(entity);
        return entity;
    }

    public List<Project> findProjectsByUserId(String token){
        Long userAccountId = this.tokenIdDecoder(token);

        List<Project> userProjects = projectRepository.findByUserAccountId(userAccountId);
        return userProjects;
    }

    public Optional<Project> findProjectByUserIdAndProjectId(Long projectId, String token){
        Long userAccountId = this.tokenIdDecoder(token);

        Optional<Project> userProject = Optional.ofNullable(projectRepository.findByUserAccountIdAndId(userAccountId, projectId));
        return userProject;
    }

    public List<ItemWithQuantity> getProjectItemsListOfPairs(Project project) throws NoSuchFieldException {

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
//        Map<Item, Integer> itemsMap = new HashMap<>();
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
