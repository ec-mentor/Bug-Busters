package bugbusters.everyonecodes.java.search;


import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VolunteerTextSearchService {

    public List<Volunteer> searchVolunteersByText(List<Volunteer> inputList, String text) {
        String lowerCaseText = text.toLowerCase(Locale.ROOT);
        var inputMap = inputList.stream()
                .collect(Collectors.toMap(volunteer -> volunteer.getId(), volunteer -> volunteer));
        var filteredMap = inputMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), volunteerToSearchString(entry.getValue())))
                .filter(entry -> entry.getValue().contains(lowerCaseText))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return filteredMap.entrySet().stream()
                .map(entry -> {
                    var minCharacterOffset = Collections.min(
                            Arrays.stream(entry.getValue().split(";"))
                            .filter(line -> line.contains(lowerCaseText))
                            .map(line -> Math.abs(line.length() - lowerCaseText.length()))
                            .collect(Collectors.toList()));
                    return Map.entry(entry.getKey(), minCharacterOffset);
                })
                .sorted(Map.Entry.comparingByValue())
                .map(entry -> inputMap.get(entry.getKey())
                ).collect(Collectors.toList());
    }

    String userToSearchString(User user) {
        String output = user.getUsername() + ";" + user.getFullName();
        if (user.getDescription() == null) return output.toLowerCase(Locale.ROOT);
        output = output + ";" + user.getDescription();
        return output.toLowerCase(Locale.ROOT);
    }

    String volunteerToSearchString(Volunteer volunteer) {
        return userToSearchString(volunteer.getUser()) + ";" + String.join(";", new HashSet<>(volunteer.getSkills())).toLowerCase(Locale.ROOT);
    }
}
