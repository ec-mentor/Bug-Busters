package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.service.LocalDateNowProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserPublicDTOServiceTest {

//    @Autowired
//    UserPublicDTOService service;
//
//    @MockBean
//    LocalDateNowProvider provider;
//
//    @MockBean
//    UserRepository userRepository;
//
//
//    @Test
//    void viewUserPublicDTO_UserFound() {
//        String username = "username";
//        Optional<User> user = Optional.of(new User(1L, username, "password", "role",
//                "fullName", LocalDate.of(1967, 8, 10), "address",
//                "email", "description"));
//        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(user);
//        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 5));
//        UserPublicDTO result = service.transformUserToUserPublicDTO(user.get());
//        Assertions.assertNotNull(result);
//    }
//
//    @Test
//    void viewUserPublicDTO_UserNotFound() {
//        String username = "username";
//        Optional<User> user = Optional.empty();
//        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(user);
//        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 5));
//        UserPublicDTO result = service.transformUserToUserPublicDTO(null);
//        Assertions.assertNull(result);
//    }
//
//
//    @Test
//    void transformUserToUserPublicDTO_allValues() {
//        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 4));
//        String username = "username";
//        String fullName = "fullname";
//        LocalDate birthday = LocalDate.of(1967, 8, 10);
//        String description = "description";
//        User user = new User(1L, username, "password", "role",
//                fullName, birthday, "address", "email", description);
//        UserPublicDTO result = service.transformUserToUserPublicDTO(user);
//        UserPublicDTO expected = new UserPublicDTO(username, fullName, 53, description);
//        Assertions.assertEquals(expected, result);
//    }
//
//    @Test
//    void transformUserToUserPublicDTO_nullValues() {
//        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 4));
//        String username = "username";
//        String fullName = "fullname";
//        User user = new User(1L, username, "password", "role",
//                fullName, null, null, null, null);
//        UserPublicDTO result = service.transformUserToUserPublicDTO(user);
//        UserPublicDTO expected = new UserPublicDTO(username, fullName, null, null);
//        Assertions.assertEquals(expected, result);
//    }
//
//
//
//    @ParameterizedTest
//    @MethodSource("parameters")
//
//    void calculateAge(LocalDate birthDate, LocalDate currentDate, Integer expected) {
//        Integer result = service.calculateAge(birthDate, currentDate);
//        Assertions.assertEquals(expected, result);
//    }
//
//    private static Stream<Arguments> parameters() {
//        return Stream.of(
//                Arguments.of(
//                        LocalDate.of(1961, 5, 17), LocalDate.of(2016, 7, 12), 55
//                ),
//                Arguments.of(
//                        LocalDate.of(1991, 8, 5), LocalDate.of(2021, 8, 4), 29
//                ),
//                Arguments.of(
//                        LocalDate.of(1991, 8, 5), LocalDate.of(2021, 8, 5), 30
//                ),
//                Arguments.of(
//                        LocalDate.of(1967, 8, 10), LocalDate.of(2021, 8, 4), 53
//                ),
//                Arguments.of(
//                        LocalDate.of(1980, 1, 1), LocalDate.of(1982, 1, 2), 2
//                )
//        );
//    }

}