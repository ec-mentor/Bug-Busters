package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ClientTextSearchServiceTest {

    @Autowired
    ClientTextSearchService clientTextSearchService;

    @ParameterizedTest
    @MethodSource("parameters_searchVolunteersByText")
    void searchVolunteersByText(List<Volunteer> input, String text, List<Volunteer> expected) {
        var result = clientTextSearchService.searchVolunteersByText(input, text);
        Assertions.assertEquals(expected, result);
    }

    //Test sometimes mess up because order can be ambiguous
    private static Stream<Arguments> parameters_searchVolunteersByText() {
        Volunteer test1 = new Volunteer(new User("Test1u", "password", "role", "Testf1", null, null, "email", null));
        test1.setId(1L);
        Volunteer test2 = new Volunteer(new User("Test2", "password", "role", "Test", null, null, "email", null));
        test2.setId(2L);
        test2.setSkills(Set.of("test2s", "skills2"));
        Volunteer test3 = new Volunteer(new User("Test1", "password", "role", "Testf3", null, null, "email", "Test3d"));
        test3.setId(3L);
        test3.setSkills(Set.of("test3s"));
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
                //only one matches, match based on Username
                Arguments.of(
                        List.of(test1, test2, test3),
                        "test1u",
                        List.of(test1)
                ),
                // 2 match, test 3 comes first
                Arguments.of(
                        List.of(test1, test2, test3),
                        "test1",
                        List.of(test3, test1)
                ),
                //match based on skills
                Arguments.of(
                        List.of(test1, test2, test3),
                        "skills",
                        List.of(test2)
                ),
                //match based on description
                Arguments.of(
                        List.of(test1, test2, test3),
                        "Test3d",
                        List.of(test3)
                ),
                //match based on fullname
                Arguments.of(
                        List.of(test1, test2, test3),
                        "Testf",
                        List.of(test1, test3)
                )
        );
    }
}