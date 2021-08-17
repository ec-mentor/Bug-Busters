package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.ActivityRepository;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDTOMapper {

    private final UserDTOMapper userDTOMapper;
    private final ActivityRepository activityRepository;

    public AdminDTOMapper(UserDTOMapper userDTOMapper, ActivityRepository activityRepository) {
        this.userDTOMapper = userDTOMapper;
        this.activityRepository = activityRepository;
    }

    public AdminDTO toAdminDTO(User user) {
        Double rating = userDTOMapper.calculateRating(user.getRatings());
        List<Activity> activities;
        if (user.getRole().equals("ROLE_VOLUNTEER")) {
            activities = activityRepository.findByVolunteer_username(user.getUsername());
        } else {
            activities = activityRepository.findByClient_username(user.getUsername());
        }
        int pending = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.PENDING))
                .count();
        int inProgress = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.IN_PROGRESS))
                .count();
        int completed = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.COMPLETED))
                .count();
        return new AdminDTO(user.getUsername(), rating, pending, inProgress, completed);
    }

    /*public AdminDTO toAdminDTO(User user) {
        Double rating = userDTOMapper.calculateRating(user.getRatings());
        List<Activity> activities = user.getActivities();
        int pending = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.PENDING))
                .count();
        int inProgress = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.IN_PROGRESS))
                .count();
        int completed = (int) activities.stream()
                .filter(e -> e.getStatusClient().equals(Status.COMPLETED))
                .count();
        return new AdminDTO(user.getUsername(), rating, pending, inProgress, completed);
    }*/
}
