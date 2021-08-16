package bugbusters.everyonecodes.java.search;


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
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().toSearchString()))
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

}
