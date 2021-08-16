package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.usermanagement.data.User;

import java.util.List;

public class ActivityDTOMapper {

    public ActivityDTO toActivityDTO(Activity activity) {
        return new ActivityDTO(activity.getTitle(),
                activity.getDescription(),
                status,
                activity.getStartDate(),
                activity.getEndDate(),
                user,
                myRatingToThem,
                myFeedbackToThem,
                theirRatingToMe,
                theirFeedbackToMe);
    }

    ActivityUserDTO toActivityDTO(User user) {
        return new ActivityUserDTO(user.getRole(), user.getUsername(), calculateRating(user.getRatings()));
    }

    Double calculateRating(List<Integer> ratings) {
        if (ratings.size() == 0) return null;
        return ratings.stream()
                .mapToDouble(Double::valueOf)
                .sum() / ratings.size();
    }

}
