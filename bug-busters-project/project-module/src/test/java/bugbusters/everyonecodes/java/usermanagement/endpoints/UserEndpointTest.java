package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    //Needed for Secured Endpoint tests, because TestRestTemplate hates me. I will answer any questions that arise - Georg
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private final String url = "/users";


    //registerUser Test

    @Test
    void registerUser_invalidPassword() {
        User testUser = new User (null, "Test",
                "invalidPassword",
                "ROLE_TEST",
                "Test Test",
                null,
                null,
                "test.test@bugbusters.com",
                null);
        Mockito.when(userService.saveUser(testUser)).thenThrow(IllegalArgumentException.class);
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url + "/register", testUser, User.class);
        HttpStatus statusCode = resultResponseEntity.getStatusCode();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        Mockito.verify(userService, Mockito.times(1)).saveUser(testUser);
    }

    @Test
    void registerUser_validationFailed() {
        User testUser = new User (null, "Test",
                "validPassword1#",
                null,
                null,
                null,
                null,
                null,
                null);
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url + "/register", testUser, User.class);
        HttpStatus statusCode = resultResponseEntity.getStatusCode();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        Mockito.verify(userService, Mockito.never()).saveUser(Mockito.any(User.class));
    }

    @Test
    void registerUser_invalidEmail() {
        User testUser = new User (null, "Test",
                "validPassword1#",
                "ROLE_TEST",
                "Test Test",
                null,
                null,
                "totallyValidEmail",
                null);
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url + "/register", testUser, User.class);
        HttpStatus statusCode = resultResponseEntity.getStatusCode();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
        Mockito.verify(userService, Mockito.never()).saveUser(Mockito.any(User.class));
    }

    @Test
    void registerUser_validNewUser() {
        User testUser = new User (null, "Test",
                "validPassword1#",
                "ROLE_TEST",
                "Test Test",
                null,
                null,
                "test.test@bugbusters.com",
                null);
        Mockito.when(userService.saveUser(testUser)).thenReturn(testUser);
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url + "/register", testUser, User.class);
        User result = resultResponseEntity.getBody();
        HttpStatus resultStatusCode = resultResponseEntity.getStatusCode();
        Assertions.assertEquals(testUser, result);
        Assertions.assertEquals(HttpStatus.OK, resultStatusCode);
        Mockito.verify(userService).saveUser(testUser);
    }


    //viewUserProfile test

    @Test
    @WithMockUser(username = "test", password = "Testing1#")
    void viewUserProfile_methodCalled() throws Exception {
        String username = "test";
        Mockito.when(userService.viewUserPrivateData(username))
                .thenReturn(Optional.of(new UserPrivateDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/login")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).viewUserPrivateData(username);
    }


    //editUserProfile Test

    @Test
    @WithMockUser(username = "test", password = "Testing1#")
    void editUserProfile_validInput() throws Exception {
        String username = "test";
        UserPrivateDTO input = new UserPrivateDTO("test", "test", "test", null, null, "test.test@test.com", null);
        Mockito.when(userService.editUserData(input, username))
                .thenReturn(Optional.of(input));
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).editUserData(input, username);
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#")
    void editUserProfile_invalidInput() throws Exception {
        String username = "test";
        UserPrivateDTO input = new UserPrivateDTO("test", "test", null, null, null, null, null);
        mockMvc.perform(MockMvcRequestBuilders.put(url + "/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(input))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        Mockito.verify(userService, Mockito.never()).editUserData(input, username);
    }


    //viewUserPublicData Test

    @Test
    @WithMockUser(username = "test", password = "Testing1#")
    void viewUserPublicData_ownData() throws Exception {
        String username = "test";
        Mockito.when(userService.viewUserPublicData(username))
                .thenReturn(Optional.of(new UserPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).viewUserPublicData(username);
    }

    @Test
    @WithMockUser(username = "test", password = "Testing1#")
    void viewUserPublicData_otherUser() throws Exception {
        String targetName = "target";
        Mockito.when(userService.viewUserPublicData(targetName))
                .thenReturn(Optional.of(new UserPublicDTO()));
        mockMvc.perform(MockMvcRequestBuilders.get(url + "/view?name=" + targetName)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Mockito.verify(userService).viewUserPublicData(targetName);
    }

}