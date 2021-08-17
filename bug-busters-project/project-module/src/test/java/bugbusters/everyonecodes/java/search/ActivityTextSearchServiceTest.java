package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityTextSearchServiceTest {

    @Autowired
    ActivityTextSearchService activityTextSearchService;

    @ParameterizedTest
    @MethodSource("parameters_searchVolunteersByText")
    void searchActivitiesByText(List<Activity> input, String text, List<Activity> expected) {
        var result = activityTextSearchService.searchActivitiesByText(input, text);
        Assertions.assertEquals(expected, result);
    }

    //Test sometimes mess up because order can be ambiguous
    private static Stream<Arguments> parameters_searchVolunteersByText() {
        Activity test1 = new Activity("Test11", "Testt1", "Testd1", Set.of(), Set.of("testC11"), LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
        test1.setId(1L);
        Activity test2 = new Activity("Test2", "Test", "Testd2", Set.of("test2s", "skills32"), Set.of("testC2"), LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
        test2.setId(2L);
        Activity test3 = new Activity("Test1", "Testt3", "Testd3", Set.of("skills3"), Set.of(), LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
        test3.setId(3L);
        return Stream.of(
                //empty case
                Arguments.of(
                        List.of(), "", List.of()
                ),
                //all match, test 2 comes first
                Arguments.of(
                        List.of(test1, test2, test3),
                        "test",
                        List.of(test2, test3, test1)
                ),
                //none match
                Arguments.of(
                        List.of(test1, test2, test3),
                        "text",
                        List.of()
                ),
                //only one matches, match based on creator
                Arguments.of(
                        List.of(test1, test2, test3),
                        "test11",
                        List.of(test1)
                ),
                // 2 match, test 3 comes first
                Arguments.of(
                        List.of(test1, test2, test3),
                        "test1",
                        List.of(test3, test1)
                ),
                //match based on skills, test 3 comes first
                Arguments.of(
                        List.of(test1, test2, test3),
                        "skills3",
                        List.of(test3, test2)
                ),
                //match based on description
                Arguments.of(
                        List.of(test1, test2, test3),
                        "Testd3",
                        List.of(test3)
                ),
                //match based on title
                Arguments.of(
                        List.of(test1, test2, test3),
                        "Testt1",
                        List.of(test1)
                ),
                //match based on categories
                Arguments.of(
                        List.of(test1, test2, test3),
                        "Testc",
                        List.of(test2, test1)
                )
        );
    }
}