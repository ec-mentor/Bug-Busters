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
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(
                        Set.of("abc", "def", "geh"), "abc; def; geh"
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

    void convertToSet(Set<String> expected, String input) {
        Set<String> result = mapper.convertToSet(input);
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> parameters2() {
        return Stream.of(
                Arguments.of(
                        Set.of("abc", "def", "geh"), "abc; def; geh"
                ),
                Arguments.of(
                        Set.of("abc"), "abc"
                ),
                Arguments.of(
                        Set.of(""), ""
                )
        );
    }
}