package com.easyvaluation.authentication.application;

import com.easyvaluation.authentication.domain.LoginService;
import com.easyvaluation.authentication.domain.refreshToken.RefreshToken;
import com.easyvaluation.authentication.domain.refreshToken.RefreshTokenRepository;
import com.easyvaluation.authentication.domain.refreshToken.RefreshTokenService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import com.easyvaluation.security.domain.UserRoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@Sql(statements = "select * from user_account")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class LoginControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LoginService loginService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @LocalServerPort
    private int port;

    private UserAccount user;
    private String url;

    @BeforeEach
    void beforeEach(){
        user = new UserAccount("jan", "lokiloki");
        user.setEmail("jan@wp.pl");
        user.setId(1L);
        user.setUserRoles(userRoleRepository.findByName("ROLE_USER"));
        url = "http://localhost:" + port + "/login";
    }

    @Test
    void contextLoads(){
        assertThat(loginService, notNullValue());
        assertThat(mockMvc, notNullValue());
        assertThat(testRestTemplate, notNullValue());
    }

    @Test
    void loginEndpointShouldReturnResponseWithTokensAnd200StatusInCaseOfProperLoginAndPass() throws Exception {
        //given

        //when
        ResponseEntity<String> response = this.testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(user), String.class);
        String responseBody = response.getBody();
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(responseBody, Map.class);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(map.get("easyValuationToken"), instanceOf(String.class));
        assertThat(map.get("easyValuationRefreshToken"), instanceOf(String.class));


    }

    @Test
    public void properUserLoginShouldReturnResponseWithValidTokenForUserAvailableEndpoint() throws Exception {
        //given

        //when
        String result = this.testRestTemplate.postForObject(url, new HttpEntity<>(user),
                String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> map = mapper.readValue(result, Map.class);

        //then
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/materials/items/1").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                .andExpect(status().isOk()).andExpect(content().json("{\"id\":1,\"version\":0,\"itemName\":\"Screw\",\"producer\":\"Elesa-Ganter\",\"symbol\":\"G12543\",\"mass\":0.1}"));
        assertThat(result, containsString("Token"));
    }

    @Test
    public void properUserLoginShouldReturnResponseWithInvalidToken403ForAdminAvailableEndpoint() throws Exception {
        //given

        //when
        String result = this.testRestTemplate.postForObject(url, user, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                        .andExpect(status().is(403));
    }

    @Test
    public void properAdminLoginShouldReturnResponseWithValidTokenForAdminAvailableEndpoint() throws Exception {
        //given
//        UserRole userRole = new UserRole();
//        user.setUserRoles(userRoleRepository.findByName("ROLE_ADMIN"));
        UserAccount admin = new UserAccount("admin", "admin");

        //when
        String result = this.testRestTemplate.postForObject(url, admin, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);

        System.out.println(map.get("easyValuationToken"));

        //then
        mockMvc.perform(
                MockMvcRequestBuilders.get("/admin").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                        .andExpect(status().is(200))
                        .andExpect(content().string("Admin panel"));
        }

    @Test
    public void improperLoginShouldReturn404Response() throws Exception {
        //given
        user.setLogin("someWeirdNotExistingLogin");

        //when
        ResponseEntity<String> response = this.testRestTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(user), String.class);
        System.out.println(response);

        //then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));

    }

    @Test
    void requestToRefreshEndpointWithValidRefreshTokenShouldReturnNewValidJwtAndRefreshToken() throws Exception {
        //given
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        //when
        String result = this.testRestTemplate.postForObject("http://localhost:" + port + "/refreshtoken", refreshToken.getToken(), String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.readValue(result, Map.class);

        //then
        MvcResult resultMvc = mockMvc.perform(
                        MockMvcRequestBuilders.post("/refreshtoken")
                                .content(refreshTokenRepository.findByUserAccount(user).getToken())
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + map.get("easyValuationToken")))
                                .andExpect(status().is(200))
                                .andReturn();

        String content = resultMvc.getResponse().getContentAsString();
        assertThat(content, is("{\"easyValuationToken\": \"" + map.get("easyValuationToken") + "\", \"easyValuationRefreshToken\": \"" + refreshTokenRepository.findByUserAccount(user).getToken() + "\"}"));
    }

    @TestFactory
    Collection<DynamicTest> endpointsLoginRefreshRegistrationH2ConsoleShouldBeAvailableWithoutToken(){
        List<String> endpoints = Arrays.asList("/login","/refreshtoken", "/user-accounts/register");
//        , "/h2-console"

        Collection<DynamicTest> dynamicTests = new ArrayList<>();

        for(int i = 0; i < endpoints.size(); i++){
            String endpoint = endpoints.get(i);
            Executable executable = new Executable() {
                @Override
                public void execute() throws Throwable {

                }
            };
            ResponseEntity<String> response;
            HttpStatus responseStatusCode;

            switch (endpoint){
                case "/login":
                    response = this.testRestTemplate.exchange("http://localhost:" + port + endpoint, HttpMethod.POST, new HttpEntity<>(user), String.class);
                    responseStatusCode = response.getStatusCode();

                    executable = () -> {
                        assertThat(responseStatusCode, equalTo(HttpStatus.OK));
                    };
                    break;

                case "/user-accounts/register":
                   user.setId(0L);
                    response = this.testRestTemplate.exchange("http://localhost:" + port + endpoint, HttpMethod.POST, new HttpEntity<>(user), String.class);
                    System.out.println(response.getBody());

                    executable = () -> {
                        assertThat(response.getStatusCode(), equalTo(HttpStatus.CONFLICT));
                        assertThat(response.getHeaders().get("Warning").get(0), equalTo("Login or email already in use"));
                    };
                    break;

//                case "/h2-console":
//                    response = this.testRestTemplate.exchange("http://localhost:" + port + endpoint, HttpMethod.GET, new HttpEntity<>(user), String.class);
//                    responseStatusCode = response.getStatusCode();
//
//                    executable = () -> {
//                        assertThat(responseStatusCode, equalTo(HttpStatus.OK));
//                    };
//                    break;
            }

            String name = "Test case: " + endpoint;
            DynamicTest dynamicTest = DynamicTest.dynamicTest(name, executable);
            dynamicTests.add(dynamicTest);
        }
        return dynamicTests;
    }

}
