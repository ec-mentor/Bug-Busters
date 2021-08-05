package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.usermanagement.data.User;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserEndpointTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @MockBean
    UserService userService;

    private final String url = "/users/register";


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
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url, testUser, User.class);
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
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url, testUser, User.class);
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
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url, testUser, User.class);
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
        ResponseEntity<User> resultResponseEntity = testRestTemplate.postForEntity(url, testUser, User.class);
        User result = resultResponseEntity.getBody();
        HttpStatus resultStatusCode = resultResponseEntity.getStatusCode();
        Assertions.assertEquals(testUser, result);
        Assertions.assertEquals(HttpStatus.OK, resultStatusCode);
        Mockito.verify(userService).saveUser(testUser);
    }


    //viewUserProfile Test
    //ToDo: create Tests

    //editUserProfile Test
    //ToDo: create Tests

    //getDTOByUsername Test
    //ToDo: create Tests
}