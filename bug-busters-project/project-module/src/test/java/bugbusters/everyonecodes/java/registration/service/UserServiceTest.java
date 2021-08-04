package bugbusters.everyonecodes.java.registration.service;

import bugbusters.everyonecodes.java.registration.data.User;
import bugbusters.everyonecodes.java.registration.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    private final User testUser = new User(1L, "test", "", "test", "test", LocalDate.parse("2000-01-01"), "test", "test", "test");

    @ParameterizedTest
    @CsvSource({
            "''", //empty String
            "'tEst123'", // Missing special char
            "'tesTIng#'", // Missing number
            "'testing1#'", // Missing Uppercase
            "'TESTING1#'", // Missing lowercase
            "'Testing 12#'", // Included whitespace
            "'tT#1'", //too short
            "'Coding123#0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890'", //Size 101 is too long
            "'testIng12§'" //Invalid Special char
    })
    void saveUser_invalidPassword(String password) {
        testUser.setPassword(password);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.saveUser(testUser));
    }

    @ParameterizedTest
    @CsvSource({
            "'Coding12#'", // simple valid Password
            "'Test0!?@#$^&+=/_-'", // verifying all valid special chars
            "'Coding123#012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789'", //MaxSize 100
            "'Test1#'" //MinSize 6
    })
    void saveUser_validPassword(String password) {
        testUser.setPassword(password);
        Mockito.when(passwordEncoder.encode(testUser.getPassword())).thenReturn(password);
        Assertions.assertDoesNotThrow(() -> userService.saveUser(testUser));
        Mockito.verify(userRepository, Mockito.times(1)).save(testUser);
    }

}