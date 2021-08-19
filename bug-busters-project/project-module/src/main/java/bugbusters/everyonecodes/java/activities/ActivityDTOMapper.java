package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityDTOMapper {

    private final UserRepository userRepository;
    private final SetToStringMapper setToStringMapper;

    public ActivityDTOMapper(UserRepository userRepository, SetToStringMapper setToStringMapper) {
        this.userRepository = userRepository;
        this.setToStringMapper = setToStringMapper;
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
                activity.getRatingFromClient(),
                activity.getFeedbackFromClient(),
                activity.getRatingFromVolunteer(),
                activity.getFeedbackFromVolunteer()
                );
    }

    public ActivityDTO toClientActivityDTO(Activity activity) {
        Optional<User> oVolunteer = userRepository.findOneByUsername(activity.getVolunteer());
        String volunteerName = null;
        String volunteerRole = null;
        Double volunteerRating = null;
        if (oVolunteer.isPresent()) {
            User volunteer = oVolunteer.get();
            volunteerName = volunteer.getUsername();
            volunteerRole = volunteer.getRole();
            volunteerRating = calculateRating(volunteer.getRatings());
        }
        return new ActivityDTO(volunteerName,
                activity.getTitle(),
                activity.getDescription(),
                activity.getStatusClient(),
                activity.getStartTime(),
                activity.getEndTime(),
                volunteerRole,
                volunteerRating,
                activity.getRatingFromClient(),
                activity.getFeedbackFromClient(),
                activity.getRatingFromVolunteer(),
                activity.getFeedbackFromVolunteer());
    }

    public Activity createNewActivityFromActivityInputDTO(ActivityInputDTO activityInputDTO, String creator) {
        Status status = activityInputDTO.getStatusClient();
        return new Activity(creator,
                activityInputDTO.getTitle(),
                activityInputDTO.getDescription(),
                setToStringMapper.convertToSet(activityInputDTO.getRecommendedSkills()),
                setToStringMapper.convertToSet(activityInputDTO.getCategories()),
                activityInputDTO.getStartTime(),
                activityInputDTO.getEndTime(),
                activityInputDTO.isOpenEnd(),
                status,
                status,
                null,
                null,
                null,
                null);
    }


    Double calculateRating(List<Integer> ratings) {
        if (ratings.size() == 0) return null;
        return ratings.stream()
                .mapToDouble(Double::valueOf)
                .sum() / ratings.size();
    }
}
