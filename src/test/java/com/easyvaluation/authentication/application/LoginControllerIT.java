package com.easyvaluation.authentication.application;

import com.easyvaluation.authentication.domain.LoginService;
import com.easyvaluation.authentication.domain.TokenProvider;
import com.easyvaluation.materialslibrary.domain.SinglePart;
import com.easyvaluation.materialslibrary.domain.item.Item;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import com.easyvaluation.security.domain.UserAccountService;
import com.easyvaluation.security.domain.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Sql(statements = "select * from user_account")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
//@WebMvcTest(LoginController.class)
public class LoginControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LoginService loginService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int port;

    @Test
    void contextLoads(){
        assertThat(loginService, notNullValue());
        assertThat(mockMvc, notNullValue());
    }

    @Test
    void loginEndpointShouldReturnResponseWithTokensAnd200StatusInCaseOfProperLoginAndPass() throws Exception {
        //given
        UserAccount user = new UserAccount();
        user.setLogin("jan");
        user.setPassword("lokiloki");

        //when
        //then
        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/login", user))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Token")));
    }

    @Test
    public void properUserLoginShouldReturnResponseWithValidTokenForUserAvailableEndpoint() throws Exception {
        //given
        UserAccount user = new UserAccount("jan", "lokiloki");

        //when
        String result = this.testRestTemplate.postForObject("http://localhost:" + port + "/login", user,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(result, Map.class);

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/materials/items/1").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                    .andExpect(status().isOk()).andExpect(content().json("{\"id\":1,\"version\":0,\"itemName\":\"srubass\",\"producer\":\"elesa\",\"symbol\":\"12543\",\"mass\":1.0}"));
        assertThat(result, containsString("Token"));
    }

    @Test
    public void properUserLoginShouldReturnResponseWithInvalidToken403ForAdminAvailableEndpoint() throws Exception {
        //given
        UserAccount user = new UserAccount("jan", "lokiloki");
//        user.setUserType(UserType.ADMIN);

        //when
        String result = this.testRestTemplate.postForObject("http://localhost:" + port + "/login", user,
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(result, Map.class);

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                    .andExpect(status().is(403));
    }

}
