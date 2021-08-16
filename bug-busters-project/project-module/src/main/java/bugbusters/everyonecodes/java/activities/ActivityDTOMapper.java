package bugbusters.everyonecodes.java.activities;

import java.util.List;

public class ActivityDTOMapper {

    public VolunteerActivityDTO toVolunteerActivityDTO(Activity activity) {
        return new VolunteerActivityDTO(activity.getTitle(),
                activity.getDescription(),
                activity.getStatusVolunteer(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getCreator().getUser().getRole(),
                activity.getCreator().getUser().getUsername(),
                calculateRating(activity.getCreator().getUser().getRatings()),
                activity.getRatingFromVolunteer(),
                activity.getFeedbackFromVolunteer(),
                activity.getRatingFromClient(),
                activity.getFeedbackFromClient());
    }

    public ClientActivityDTO toClientActivityDTO(Activity activity) {
        return new ClientActivityDTO(activity.getTitle(),
                activity.getDescription(),
                activity.getStatusClient(),
                activity.getStartTime(),
                activity.getEndTime(),
                activity.getVolunteer().getUser().getRole(),
                activity.getVolunteer().getUser().getUsername(),
                calculateRating(activity.getVolunteer().getUser().getRatings()),
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
