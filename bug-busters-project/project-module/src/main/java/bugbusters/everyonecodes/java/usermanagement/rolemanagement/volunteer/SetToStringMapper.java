package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SetToStringMapper {

    public String convertToString(Set<String> input) {
        if (input == null || input.isEmpty()) return null;
        input = new HashSet<>(input);
        return String.join("; ", input);
    }


    public Set<String> convertToSet(String input) {
        if (input == null || input.isEmpty()) return null;
        String[] split = input.split(";");
        List<String> splitList = Arrays.asList(split);
        splitList = splitList.stream().map(String::trim).collect(Collectors.toList());
        return new HashSet<>(splitList);
    }
}
