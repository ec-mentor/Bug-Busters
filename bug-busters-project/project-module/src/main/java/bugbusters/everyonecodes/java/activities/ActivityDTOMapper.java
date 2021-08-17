package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityDTOMapper {

    private final UserRepository userRepository;

    public ActivityDTOMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ActivityDTO toVolunteerActivityDTO(Activity activity) {
        Optional<User> oClient = userRepository.findOneByUsername(activity.getCreator());
        if (oClient.isEmpty()) return null;
        User client = oClient.get();
        return new ActivityDTO(activity.getCreator(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getStatusVolunteer(),
                activity.getStartTime(),
                activity.getEndTime(),
                client.getRole(),
                calculateRating(client.getRatings()),
                activity.getRatingFromVolunteer(),
                activity.getFeedbackFromVolunteer(),
                activity.getRatingFromClient(),
                activity.getFeedbackFromClient()
                );
    }

    public ActivityDTO toClientActivityDTO(Activity activity) {
        Optional<User> oVolunteer = userRepository.findOneByUsername(activity.getVolunteer());
        if (oVolunteer.isEmpty()) return null;
        User volunteer = oVolunteer.get();
        return new ActivityDTO(activity.getVolunteer(),
                activity.getTitle(),
                activity.getDescription(),
                activity.getStatusClient(),
                activity.getStartTime(),
                activity.getEndTime(),
                volunteer.getRole(),
                calculateRating(volunteer.getRatings()),
                activity.getRatingFromClient(),
                activity.getFeedbackFromClient(),
                activity.getRatingFromVolunteer(),
                activity.getFeedbackFromVolunteer());
    }


    Double calculateRating(List<Integer> ratings) {
        if (ratings.size() == 0) return null;
        return ratings.stream()
                .mapToDouble(Double::valueOf)
                .sum() / ratings.size();
    }
}
