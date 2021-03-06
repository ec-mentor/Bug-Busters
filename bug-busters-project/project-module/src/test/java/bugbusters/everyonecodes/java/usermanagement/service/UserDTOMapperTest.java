package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
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
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserDTOMapperTest {

    @Autowired
    UserDTOMapper userDTOMapper;

    @MockBean
    LocalDateNowProvider provider;


    //toUserPrivateDTO Test

    @Test
    void toUserPrivateDTO(){
        User user = new User(
                "username",
                "password",
                "role",
                "fullname",
                LocalDate.now(),
                "address",
                "email",
                "description");
        UserPrivateDTO result = userDTOMapper.toUserPrivateDTO(user);
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
    void testUserToPrivateUserDTO_nullValues(){
        User user = new User(
                "username",
                "password",
                "role",
                null,
                null,
                null,
                null,
                "description");
        UserPrivateDTO result = userDTOMapper.toUserPrivateDTO(user);
        UserPrivateDTO expected = new UserPrivateDTO("username",
                "role",
                null,
                null,
                null,
                null,
                "description");
        Assertions.assertEquals(expected, result);
    }


    //toUserPublicDTO Test

    @Test
    void toUserPublicDTO() {
        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 4));
        String username = "username";
        String fullName = "fullname";
        LocalDate birthday = LocalDate.of(1967, 8, 10);
        String description = "description";
        List<Integer> ratings = List.of(2, 4, 4);
        Double rating = userDTOMapper.calculateRating(ratings);
        User user = new User(username, "password", "role",
                fullName, birthday, "address", "email", description);
        UserPublicDTO result = userDTOMapper.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(username, fullName, 53, description, rating, 0);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void toUserPublicDTO_nullValues() {
        Mockito.when(provider.getDateNow()).thenReturn(LocalDate.of(2021, 8, 4));
        String username = "username";
        String fullName = "fullname";
        User user = new User(username, "password", "role",
                fullName, null, null, null, null);
        UserPublicDTO result = userDTOMapper.toUserPublicDTO(user);
        UserPublicDTO expected = new UserPublicDTO(username, fullName, null, null, null, 0);
        Assertions.assertEquals(expected, result);
    }


    //calculateAge Test

    @ParameterizedTest
    @MethodSource("parameters_calculateAge")
    void calculateAge(LocalDate birthDate, LocalDate currentDate, Integer expected) {
        Integer result = userDTOMapper.calculateAge(birthDate, currentDate);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> parameters_calculateAge() {
        return Stream.of(
                Arguments.of(
                        LocalDate.of(1961, 5, 17), LocalDate.of(2016, 7, 12), 55
                ),
                Arguments.of(
                        LocalDate.of(1991, 8, 5), LocalDate.of(2021, 8, 4), 29
                ),
                Arguments.of(
                        LocalDate.of(1991, 8, 5), LocalDate.of(2021, 8, 5), 30
                ),
                Arguments.of(
                        LocalDate.of(1967, 8, 10), LocalDate.of(2021, 8, 4), 53
                ),
                Arguments.of(
                        LocalDate.of(1980, 1, 1), LocalDate.of(1982, 1, 2), 2
                )
        );
    }


    //calculateRating Tests

    @Test
    void calculateRating_emptyList() {
        List<Integer> input = List.of();
        Double result = userDTOMapper.calculateRating(input);
        Assertions.assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("parameters_calculateRating")
    void calculateRating_ListWithElements(List<Integer> input, Double expected) {
        Double result = userDTOMapper.calculateRating(input);
        Assertions.assertEquals(expected, result, 0.0000001);
    }

    private static Stream<Arguments> parameters_calculateRating() {
        return Stream.of(
                Arguments.of(
                        List.of(2, 2, 3), 2.33333333
                ),
                Arguments.of(
                        List.of(1, 2), 1.5
                ),
                Arguments.of(
                        List.of(1), 1.0
                )
        );
    }
}