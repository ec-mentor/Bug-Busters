package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilterVolunteerService {
    private final UserDTOMapper userDTOMapper;
    private final VolunteerTextSearchService volunteerTextSearchService;

    public FilterVolunteerService(UserDTOMapper userDTOMapper, VolunteerTextSearchService volunteerTextSearchService) {
        this.userDTOMapper = userDTOMapper;
        this.volunteerTextSearchService = volunteerTextSearchService;
    }

    public List<Volunteer> filterSearchResults(List<Volunteer> searchResults, FilterVolunteer filterVolunteer){
        return searchResults.stream()
                .filter(searchResult ->
                        filterSkills(searchResult.getSkills(), filterVolunteer.getSkills()))
                .filter(searchResult -> filterRating(searchResult.getUser().getRatings(), filterVolunteer.getRating()))
                .collect(Collectors.toList());
    }

    private boolean filterSkills(Set<String> skills, String input){
        if(input == null){
            return true;
        }
        String x = String.join(";", skills);
        return x.contains(input);
    }
    private boolean filterRating(List<Integer> ratings, Integer input){
        if(input == null){
            return true;
        }
        return (userDTOMapper.calculateRating(ratings) >= Double.valueOf(input));
    }
}
