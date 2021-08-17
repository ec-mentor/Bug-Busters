package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDTOMapper {

    private final UserDTOMapper userDTOMapper;

    public AdminDTOMapper(UserDTOMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }

    public AdminDTO toAdminDTO(User user) {
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
    }

}
