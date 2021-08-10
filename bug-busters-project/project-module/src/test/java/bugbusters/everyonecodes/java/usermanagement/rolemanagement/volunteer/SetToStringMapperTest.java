package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SetToStringMapperTest {

    @Autowired
    SetToStringMapper mapper;


    @ParameterizedTest
    @MethodSource("parameters")

    void convertToString(Set<String>input, String expected) {
        String result = mapper.convertToString(input);
        Assertions.assertTrue(result.contains(expected));
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(
                        Set.of("abc", "def", "geh"), "abc; def; geh"
                ),
                Arguments.of(
                        Set.of("abc", "def"), "abc; def"
                ),
                Arguments.of(
                        Set.of("abc"), "abc"
                ),
                Arguments.of(
                        Set.of(""), ""
                )
        );
    }

    @ParameterizedTest
    @MethodSource("parameters2")

    void convertToSet(String input, Set<String> expected) {
        Set<String> result = mapper.convertToSet(input);
        Assertions.assertEquals(result, expected);
    }

    private static Stream<Arguments> parameters2() {
        return Stream.of(
                Arguments.of(
                        "abc; def; geh", Set.of("abc", "def", "geh")
                        ),
                Arguments.of(
                        "abc; def", Set.of("abc", "def")
                        ),
                Arguments.of(
                        "abc", Set.of("abc")
                        ),
                Arguments.of(
                        "", Set.of("")
                        )
        );
    }

}