package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FilterActivityServiceTest {

    @Autowired
    FilterActivityService filterActivityService;

    @MockBean
    UserDTOMapper userDTOMapper;

    @SpyBean
    UserRepository userRepository;

    @ParameterizedTest
    @MethodSource("inputParams_filterSearchResults")
    void filterSearchResults(List<Activity> searchResults, List<User> creators, FilterActivity filterActivity, List<Activity> expected) {
        Mockito.when(userDTOMapper.calculateRating(Mockito.anyList())).thenCallRealMethod();
        Mockito.when(userRepository.findOneByUsername(Mockito.any(String.class))).thenAnswer(invocation -> {
            String argument = String.valueOf(invocation.getArguments()[0]);
            return creators.stream()
                    .filter(user -> user.getUsername().equals(argument))
                    .findFirst();
        });
        List<Activity> result = filterActivityService.filterSearchResults(searchResults, filterActivity);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> inputParams_filterSearchResults() {
        Activity activity1 = new Activity(
                "user1", "activity1-1", "description1-1", Set.of("a", "b", "c"), Set.of("x", "y", "z"),
                LocalDateTime.of(2000, 1, 1, 0, 0), LocalDateTime.of(2000, 1, 2, 0, 0), false, Status.PENDING, Status.PENDING, null, null, null, null
        );
        Activity activity2 = new Activity(
                "user2", "activity2-1", "description2-1", Set.of("a", "b", "d"), Set.of("y", "z"),
                LocalDateTime.of(2000, 1, 1, 12, 0), LocalDateTime.of(2000, 1, 2, 0, 0), false, Status.PENDING, Status.PENDING, null, null, null, null
        );
        Activity activity3 = new Activity(
                "user1", "activity1-2", "description1-2", Set.of("g"), Set.of("x", "w", "z"),
                LocalDateTime.of(2000, 2, 1, 0, 0), LocalDateTime.of(2000, 2, 2, 0, 0), false, Status.PENDING, Status.PENDING, null, null, null, null
        );
        Activity activity4 = new Activity(
                "user3", "activity3-1", "description3-1", Set.of("a", "c"), Set.of("w"),
                LocalDateTime.of(2000, 2, 1, 12, 0), LocalDateTime.of(2000, 2, 2, 0, 0), false, Status.PENDING, Status.PENDING, null, null, null, null
        );
        Activity activity5 = new Activity(
                "user4", "activity4-1", "description4-1", Set.of("a", "c", "f"), Set.of("y", "v", "u"),
                LocalDateTime.of(2000, 3, 1, 0, 0), LocalDateTime.of(2000, 3, 2, 0, 0), false, Status.PENDING, Status.PENDING, null, null, null, null
        );

        List<Activity> input = List.of(activity1, activity2, activity3, activity4, activity5);

        User user1 = new User();
        user1.setUsername("user1");
        user1.setRatings(List.of(5));

        User user2 = new User();
        user2.setUsername("user2");
        user2.setRatings(List.of(2));

        User user3 = new User();
        user3.setUsername("user3");
        user3.setRatings(List.of(3));

        User user4 = new User();
        user4.setUsername("user4");
        user4.setRatings(List.of(4));

        List<User> creators = List.of(user1, user2, user3, user4);

        return Stream.of(
                //no filter returns input
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                null,
                                null,
                                null,
                                null,
                                null
                        ),
                        input
                ),
                //filter date
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                LocalDateTime.of(2000, 1, 1, 12, 0),
                                null,
                                null,
                                null,
                                null
                        ),
                        List.of(activity1, activity2)
                ),
                //filter category
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                null,
                                "w",
                                null,
                                null,
                                null
                        ),
                        List.of(activity3, activity4)
                ),
                //filter skills
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                null,
                                null,
                                "c",
                                null,
                                null
                        ),
                        List.of(activity1, activity4, activity5)
                ),
                //filter creator
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                null,
                                null,
                                null,
                                "user1",
                                null
                        ),
                        List.of(activity1, activity3)
                ),
                //filter rating
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                null,
                                null,
                                null,
                                null,
                                3
                        ),
                        List.of(activity1, activity3, activity4, activity5)
                ),
                //filter everything
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                LocalDateTime.of(2000, 2, 1, 0, 0),
                                "z",
                                "g",
                                "user1",
                                3
                        ),
                        List.of(activity3)
                ),
                //no match for filters
                Arguments.of(
                        input, creators,
                        new FilterActivity(
                                LocalDateTime.of(1999, 1, 1, 0, 0),
                                "no",
                                "no",
                                "no",
                                5
                        ),
                        List.of()
                )
        );
    }

}