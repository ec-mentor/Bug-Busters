package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.RoleFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleFactory roleFactory;

    @MockBean
    UserDTOMapper mapper;

    private final User testUser = new User(1L, "test", "", "test", "test", LocalDate.parse("2000-01-01"), "test", "test", "test");


    //saveUser Tests

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


    //editUserData Test

    @Test
    void editUserDataEmpty() {
        UserPrivateDTO userChanges = new UserPrivateDTO("1", "2", "3", LocalDate.of(1999,8,10), "4", "abc@def.g", "5");
        Mockito.when(userRepository.findOneByUsername("1")).thenReturn(Optional.empty());
        var result = userService.editUserData(userChanges, "1");
        Assertions.assertEquals(Optional.empty(), result);
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    @Test
    void editUserDataFound() {
        User oldUser = new User(1L, "1", "", "2", "3", LocalDate.of(2000, 1, 1), "4", "abcee@def.g", "test");
        User newUser = new User(1L, "1", "", "2", "newName", LocalDate.of(1999,8,10), "newAddress", "abc@def.g", "newDescription");
        UserPrivateDTO userChanges = new UserPrivateDTO("1", "2", "newName", LocalDate.of(1999,8,10), "newAddress", "abc@def.g", "newDescription");
        Mockito.when(userRepository.findOneByUsername("1")).thenReturn(Optional.of(oldUser));
        Mockito.when(mapper.toUserPrivateDTO(Mockito.any(User.class))).thenReturn(new UserPrivateDTO());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(new User());
        userService.editUserData(userChanges, "1");
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser);
    }

    //viewUserPrivateData Test

    @Test
    void viewUserPrivateData_UserFound() {
        String username = "username";
        User user = new User(1L, username, "test", "test",
                "test", LocalDate.of(2000, 1, 1), "test",
                "test", "test");
        UserPrivateDTO userPrivateDTO = new UserPrivateDTO(username, user.getRole(), user.getFullName(), user.getBirthday(), user.getAddress(), user.getEmail(), user.getDescription());
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(mapper.toUserPrivateDTO(user)).thenReturn(userPrivateDTO);
        var oResult = userService.viewUserPrivateData(username);
        Assertions.assertEquals(Optional.of(userPrivateDTO), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(mapper, Mockito.times(1)).toUserPrivateDTO(user);
    }

    @Test
    void viewUserPrivateData_UserNotFound() {
        String username = "test";
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPrivateData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(mapper, Mockito.never()).toUserPrivateDTO(Mockito.any(User.class));
    }


    //getUserByUsername Test

    @Test
    void getUserByUsername() {
        String username = "test";
        userService.getUserByUsername(username);
        Mockito.verify(userRepository).findOneByUsername(username);
    }


    //viewUserPublicDTO Test

    @Test
    void viewUserPublicData_UserFound() {
        String username = "username";
        User user = new User(1L, username, "test", "test",
                "test", LocalDate.of(2000, 1, 1), "test",
                "test", "test");
        UserPublicDTO userPublicDTO = new UserPublicDTO(username, "test", 1, "test", null);
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        Mockito.when(mapper.toUserPublicDTO(user)).thenReturn(userPublicDTO);
        var oResult = userService.viewUserPublicData(username);
        Assertions.assertEquals(Optional.of(userPublicDTO), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(mapper, Mockito.times(1)).toUserPublicDTO(user);
    }

    @Test
    void viewUserPublicData_UserNotFound() {
        String username = "test";
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        var oResult = userService.viewUserPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(userRepository, Mockito.times(1)).findOneByUsername(username);
        Mockito.verify(mapper, Mockito.never()).toUserPublicDTO(Mockito.any(User.class));
    }
}