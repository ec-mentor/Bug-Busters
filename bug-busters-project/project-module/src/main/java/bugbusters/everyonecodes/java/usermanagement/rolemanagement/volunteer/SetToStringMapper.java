package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;

@Service
public class SetToStringMapper {

    public String convertToString(Set<String> input) {
        return String.join("; ", input);
    }


    public Set<String> convertToSet(String input) {
        String[] split = input.split(";");
        return Set.of(String.valueOf(Arrays.stream(split).map(String::trim)));
    }
}
