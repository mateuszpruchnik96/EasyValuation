package com.easyvaluation.projects.application;

import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.projects.domain.Operation;
import com.easyvaluation.projects.domain.Project;
import com.easyvaluation.projects.domain.Project2;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    Project2 project = new Project2();

    @BeforeEach
    void x() throws JsonProcessingException {

        Item screw = new SinglePart("Elesa-Ganter", "G12543", 0.1F);
        screw.setItemName("Screw");
//        itemRepository.save(screw);


        Item bolt = new SinglePart("Kipp", "XXXX", 0.12F);
        bolt.setItemName("Bolt");
//        itemRepository.save(bolt);


        project.addItem(1L, 23);
        project.addItem(2L, 213);

    }

    @Test
    void xddd() throws JsonProcessingException {
        System.out.println(objectMapper.writeValueAsString(project));
    }


}