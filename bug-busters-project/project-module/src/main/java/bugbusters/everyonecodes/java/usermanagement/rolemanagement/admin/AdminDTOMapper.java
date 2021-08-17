package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDTOMapper {

    public AdminDTO toAdminDTO(User user) {
        List<Integer> ratings = user.getRatings();
        Double rating = null;
        if (ratings.size() > 0) {
            rating = ratings.stream()
                    .mapToDouble(Double::valueOf)
                    .sum() / ratings.size();
        }
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
    }

}
