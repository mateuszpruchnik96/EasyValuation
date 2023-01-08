package com.easyvaluation.projects.domain;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.materialslibrary.domain.item.ItemService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class ProjectServiceTest {


    UserAccount user = new UserAccount("jan", "lokiloki");
    UserRole userRole = new UserRole("ROLE_USER");
    Item screw = new SinglePart("Elesa-Ganter", "G12543", 0.1F);
    Item bolt = new SinglePart("Elesa-Ganter", "G1111", 0.2F);
    Operation operation1 = new Operation();
    Operation operation2 = new Operation();
    Project project = new Project();
    List<Long> itemIds= new ArrayList<>();


    @BeforeEach
    void init() throws JsonProcessingException {

        user.setEmail("jan@wp.pl");
        user.setId(1L);
        userRole.setId(1L);
        user.setUserRoles(userRole);

        screw.setItemName("Screw");
        screw.setId(1L);

        screw.setItemName("Bolt");
        bolt.setId(2L);

        operation1.setDescription("Operation 1");
        operation1.setHourPrice(100);
        operation1.setHours(10);

        operation2.setDescription("Operation 2");
        operation2.setHourPrice(200);
        operation2.setHours(20);

        project.setUserAccount(user);

        project.addOperation(operation1);
        project.addOperation(operation2);
        project.addItem(screw.getId(), 3);
        project.addItem(bolt.getId(),20);
        itemIds.add(screw.getId());
        itemIds.add(bolt.getId());
    }

    @Mock
    ItemService itemService;

    @InjectMocks
    ProjectService projectService;


//    @Test
//    void getProjectItemsMapShouldReturnProperMap() throws NoSuchFieldException {
//        //given
//        ArrayList<Long> itemsIds = new ArrayList<>();
//        itemsIds.add(1L);
//        itemsIds.add(2L);
//
//        List<Item> itemsList = new ArrayList<>();
//        itemsList.add(screw);
//        itemsList.add(bolt);
//
//        given(itemService.findItemsById(itemsIds)).willReturn(itemsList);
//
//        //when
//        Map<Item, Integer> itemsMap = projectService.getProjectItemsMap(project);
//        System.out.println(itemsMap);
//
//        //then
//        assertThat(itemsMap, notNullValue());
//        assertThat(itemsMap.get(screw), is(3));
//        assertThat(itemsMap.get(bolt), is(20));
//    }
}