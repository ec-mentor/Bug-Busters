package bugbusters.everyonecodes.java.profile.userprivate;

import bugbusters.everyonecodes.java.registration.data.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserPrivateDTOServiceTest {

    @Autowired
    UserPrivateDTOService userPrivateDTOService;

    @Test
    void testUserToPrivateUserDTO_rightValues(){
        User user = new User(123L,
                "username",
                "password",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        UserPrivateDTO result = userPrivateDTOService.toDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUserToPrivateUserDTO_wrongValues(){
        User user = new User(123L,
                "username",
                "password",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        UserPrivateDTO result = userPrivateDTOService.toDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("mike",
                "user",
                "michael",
                LocalDate.now(),
                "vienna",
                "email",
                "coding");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testUserToPrivateUserDTO_nullValues(){
        User user = new User(123L,
                "username",
                "password",
                "role",
                null,
                null,
                null,
                null,
                "description");
        UserPrivateDTO result = userPrivateDTOService.toDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                null,
                null,
                null,
                null,
                "description");
        Assertions.assertEquals(expected, result);
    }

}
