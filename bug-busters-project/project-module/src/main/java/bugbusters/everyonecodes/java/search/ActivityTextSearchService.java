package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.usermanagement.data.User;

import java.util.*;
import java.util.stream.Collectors;

public class ActivityTextSearchService {

    public List<Activity> searchVolunteersByText(List<Activity> inputList, String text) {
        String lowerCaseText = text.toLowerCase(Locale.ROOT);
        var inputMap = inputList.stream()
                .collect(Collectors.toMap(activity -> activity.getId(), activity -> activity));
        var filteredMap = inputMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), activityToSearchString(entry.getValue())))
                .filter(entry -> entry.getValue().contains(lowerCaseText))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        //duplicated code in both searchServices, can be refactored out of method
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

    String activityToSearchString(Activity activity) {
        User user = activity.getCreator().getUser();
        String output = userToSearchString(user);
        return output + ";" + activity.getTitle() + ";" +
                activity.getDescription();
    }
}
