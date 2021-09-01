package bugbusters.everyonecodes.java.search;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilterActivityService {

    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;

    public FilterActivityService(UserDTOMapper userDTOMapper, UserRepository userRepository) {
        this.userDTOMapper = userDTOMapper;
        this.userRepository = userRepository;
    }

    public List<Activity> filterSearchResults(List<Activity> searchResults, FilterActivity filterActivity){
        return searchResults.stream()
                .filter(searchResult -> filterDate(searchResult.getStartTime(), searchResult.getEndTime(), filterActivity.getDate()))
                .filter(searchResult -> filterCategories(searchResult.getCategories(), filterActivity.getCategory()))
                .filter(searchResult -> filterSkills(searchResult.getRecommendedSkills(), filterActivity.getSkills()))
                .filter(searchResult -> filterCreator(searchResult.getCreator(), filterActivity.getCreator()))
                .filter(searchResult -> filterRating(searchResult.getCreator(), filterActivity.getRating()))
                .collect(Collectors.toList());
    }

    private boolean filterDate(LocalDateTime startDate, LocalDateTime endDate, LocalDateTime inputDate) {
        if (inputDate == null || inputDate.equals(startDate) || inputDate.equals(endDate)) return true;
        return startDate.isBefore(inputDate) && endDate.isAfter(inputDate);
    }

    private boolean filterCategories(Set<String> categories, String input) {
        if(input == null){
            return true;
        }
        String x = String.join(";", categories);
        return x.contains(input);
    }

    private boolean filterSkills(Set<String> skills, String input){
        if(input == null){
            return true;
        }
        String x = String.join(";", skills);
        return x.contains(input);
    }

    private boolean filterCreator(String creator, String inputCreator) {
        if(inputCreator == null){
            return true;
        }
        return creator.equals(inputCreator);
    }

    private boolean filterRating(String creatorName, Integer input){
        if(input == null){
            return true;
        }
        var creator = userRepository.findOneByUsername(creatorName);
        if (creator.isEmpty()) return false;
        var ratings = creator.get().getRatings();
        Double rating = userDTOMapper.calculateRating(ratings);
        if (rating == null) return false;
        return (rating >= Double.valueOf(input));
    }

}
