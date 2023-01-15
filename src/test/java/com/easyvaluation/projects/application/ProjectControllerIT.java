package com.easyvaluation.projects.application;

import com.easyvaluation.materialslibrary.domain.item.ItemRepository;
import com.easyvaluation.projects.domain.Operation;
import com.easyvaluation.projects.domain.Project;

import com.easyvaluation.projects.domain.ProjectRepository;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProjectControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    private UserAccount user = new UserAccount();
    private String url;

    @Test
    void contextLoads(){
        assertThat(userAccountRepository, notNullValue());
        assertThat(mockMvc, notNullValue());
        assertThat(itemRepository, notNullValue());
        assertThat(testRestTemplate, notNullValue());
    }

    Map map;

    // Will last 20.1.2023
    String jwtTokenLong = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiamFuIiwicm9sZSI6IlJPTEVfVVNFUiwgIiwidXNlcklkIjoxLCJpYXQiOjE2NzM3Nzk0NjUsImV4cCI6MTY3NDE4OTUzMX0.cFwN1AdVC9tIbvbCGmTxn9EhtLYeqfoFFpkhHOO-x1hlmFJKxBS-Vua7ArxP3ZzjfBvrhz3Xw2l4fSyT9VM1hA";

//    @BeforeEach
//    void init() throws JsonProcessingException {
//        url = "http://localhost:" + port + "/login";
//        user = userAccountRepository.findById(1L).get();
////        ResponseEntity<String> response = this.testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(user), String.class);
//        String result = this.testRestTemplate.postForObject("http://localhost:" + port + "/login", user, String.class);
////        String responseBody = response.getBody();
//        ObjectMapper mapper = new ObjectMapper();
//        map = mapper.readValue(result, Map.class);
//
//    }

    @AfterEach
    void clear(){

    }

    @Test
    void projectsEndpointShouldReturnSavedProjectAndOkAfterPostNewProject() throws Exception {
        //given
        Operation operation1 = new Operation();
        operation1.setDescription("Operation 1");
        operation1.setHourPrice(100);
        operation1.setHours(10);

        Operation operation2 = new Operation();
        operation2.setDescription("Operation 2");
        operation2.setHourPrice(200);
        operation2.setHours(20);

        Project project = new Project();
        user = userAccountRepository.findById(1L).get();

        project.setUserAccount(user);
        Long itemId = itemRepository.findByItemNameStartsWithIgnoreCase("Screw").get(0).getId();
        try {
            project.addItem(itemId, 2);
//            project.addItem(itemRepository.findByItemNameStartsWithIgnoreCase("Bolt").get(0).getId(), 10);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        project.addOperation(operation1);
        project.addOperation(operation2);

        url = "http://localhost:" + port + "/projects";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        //when

        //then
            MvcResult resultx = mockMvc.perform(
                        MockMvcRequestBuilders.post("/projects")
                                .content(objectMapper.writeValueAsString(project))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + jwtTokenLong))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    objectMapper.readValue( result.getResponse().getContentAsString(), Project.class).getOperationList().get(0).getDescription().equals("Operation 1");
                })
                    .andExpect(result -> {
                        objectMapper.readValue( result.getResponse().getContentAsString(), Project.class).getOperationList().get(1).equals(operation2);
                    })
                .andExpect(result -> {
                    objectMapper.readValue( result.getResponse().getContentAsString(), Project.class).getItems().containsKey(itemId);
                })
                .andExpect(result -> {
                    objectMapper.readValue( result.getResponse().getContentAsString(), Project.class).getItems().get(itemId).equals(2);
                })
                .andReturn()
                ;

        projectRepository.deleteById(objectMapper.readValue(resultx.getResponse().getContentAsString(), Project.class).getId());
    }


}