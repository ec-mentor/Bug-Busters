package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    @MockBean
    UserDTOMapper userDTOMapper;

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
    //ToDo refactor Endpoint test using TestRestTemplate

//    @Test
//    @WithMockUser(username = "test", password = "Testing1#")
//    void viewUserProfile_methodCalled(){
//        String username = "test";
//        String password = "Testing1#";
//
//        Mockito.when(userService.viewUserPrivateData(username))
//                .thenReturn(Optional.of(new UserPrivateDTO()));
//        ResponseEntity<UserPrivateDTO> response = testRestTemplate.getForEntity(url + "/login", UserPrivateDTO.class);
//        System.out.println(response.getStatusCode());
//        Mockito.verify(userService).viewUserPrivateData(username);
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//    }


    //editUserProfile Test
    //ToDo create validation testcase
    //ToDo refactor Endpoint test using TestRestTemplate

    @Test
    void editUserProfile_methodCalled(){
        String inputString = "string";
        UserPrivateDTO input = new UserPrivateDTO("username", "test", "full name", null, "address", "email", "description");
        Mockito.when(userService.editUserData(input, inputString))
                .thenReturn(Optional.empty());
        Optional<UserPrivateDTO> result = userService.editUserData(input, inputString);
        Mockito.verify(userService).editUserData(input, inputString);
        Assertions.assertEquals(result, Optional.empty());
    }


    //viewUserPublicData Test
    //ToDo refactor Endpoint test using TestRestTemplate

    @Test
    void viewUserPublicData_methodCalled(){
        String input = "string";
        Mockito.when(userService.viewUserPublicData(input))
                .thenReturn(Optional.empty());
        Optional<UserPublicDTO> result = userService.viewUserPublicData(input);
        Mockito.verify(userService).viewUserPublicData(input);
        Assertions.assertEquals(result, Optional.empty());
    }

}