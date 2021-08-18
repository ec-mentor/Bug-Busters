package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SetToStringMapper setToStringMapper;
    private final UserRepository userRepository;
    private final ActivityDTOMapper activityDTOMapper;
    private final NotificationService notificationService;

    public ActivityService(ActivityRepository activityRepository, SetToStringMapper setToStringMapper, UserRepository userRepository, ActivityDTOMapper activityDTOMapper, NotificationService notificationService) {
        this.activityRepository = activityRepository;
        this.setToStringMapper = setToStringMapper;
        this.userRepository = userRepository;
        this.activityDTOMapper = activityDTOMapper;
        this.notificationService = notificationService;
    }

    public Optional<Activity> saveNewActivity(ActivityInputDTO activityInputDTO, String username) {
        Activity activity = activityDTOMapper.createNewActivityFromActivityInputDTO(activityInputDTO, username);
        var oCreator = userRepository.findOneByUsername(username);
        if (oCreator.isPresent()) {
            activity = activityRepository.save(activity);
            var creator = oCreator.get();
            creator.getActivities().add(activity);
            userRepository.save(creator);
            return Optional.of(activity);
        }
        return Optional.empty();
    }


    public Optional<Activity> postDraft(Long id) {
        var oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oActivity.get();
        result.setStatusClient(Status.PENDING);
        result.setStatusVolunteer(Status.PENDING);
        return Optional.of(activityRepository.save(result));
    }


    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Optional<Activity> edit(ActivityInputDTO input, Long id, String username) {
        Optional<Activity> oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oActivity.get();
        if (!result.getCreator().equals(username)) return Optional.empty();
        result.setTitle(input.getTitle());
        result.setDescription(input.getDescription());
        result.setRecommendedSkills(setToStringMapper.convertToSet(input.getRecommendedSkills()));
        result.setCategories(setToStringMapper.convertToSet(input.getCategories()));
        result.setStartTime(input.getStartTime());
        result.setEndTime(input.getEndTime());
        result.setOpenEnd(input.isOpenEnd());
        return Optional.of(activityRepository.save(result));
    }

    public List<Activity> findAllPendingActivities() {
        return activityRepository.findAllByStatusClient(Status.PENDING);
    }

    public Optional<Activity> completeActivityClientNotifyVolunteer(Long id, int rating, String feedback) {
        Optional<Activity> oResult = activityRepository.findById(id);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        result.setStatusClient(Status.COMPLETED);
        result.setRatingFromClient(rating);
        if (!feedback.isEmpty()) {
            result.setFeedbackFromClient(feedback);
        }
        String message = "Congratulations, you have completed the activity: " + result.getTitle() + " from " + result.getCreator() + ". You have been rated: " + rating + ". To finish the process you have to rate the organization and you can give optional feedback!";
        Notification notification = new Notification(result.getCreator(), message);
        String volunteerUsername = result.getVolunteer();
        notificationService.saveNotification(notification, volunteerUsername);
        return Optional.of(activityRepository.save(result));
    }

    public Optional<Activity> completeActivityVolunteer(Long id, int rating, String feedback) {
        Optional<Activity> oResult = activityRepository.findById(id);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        if (!result.getStatusClient().equals(Status.COMPLETED)) {
            return Optional.empty();
        }
        result.setStatusVolunteer(Status.COMPLETED);
        result.setRatingFromVolunteer(rating);
        if (!feedback.isEmpty()) {
            result.setFeedbackFromVolunteer(feedback);
        }
        return Optional.of(activityRepository.save(result));
    }

    public Optional<Activity> applyForActivity(Long activityId, String username) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        if (!result.getStatusVolunteer().equals(Status.PENDING)) {
            return Optional.empty();
        }
        Optional<User> volunteer = userRepository.findOneByUsername(username);
        if (volunteer.isEmpty()) {
            return Optional.empty();
        }
        result.getApplicants().add(username);
        volunteer.get().getActivities().add(result);
        userRepository.save(volunteer.get());
        String message = username + " applied for your activity \"" + result.getTitle() + "\"!";
        Notification notification = new Notification(username, message);
        notificationService.saveNotification(notification, result.getCreator());
        return Optional.of(activityRepository.save(result));
    }

    public Optional<Activity> approveApplication(Long activityId, String username) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        if (!result.getApplicants().contains(username)) {
            return Optional.empty();
        }
        result.setStatusVolunteer(Status.IN_PROGRESS);
        result.setStatusClient(Status.IN_PROGRESS);
        result.getApplicants()
                .forEach(applicant -> {
                            var volunteer = userRepository.findOneByUsername(applicant);
                            volunteer.ifPresent(user -> {
                                user.getActivities().remove(result);
                                userRepository.save(volunteer.get());
                            });
                        }
                );
        return Optional.of(activityRepository.save(result));
    }

    public void denyApplication(Long activityId, String username) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return;
        }
        Activity result = oResult.get();
        if (!result.getApplicants().contains(username)) {
            return;
        }
        Optional<User> volunteer = userRepository.findOneByUsername(username);
        if (volunteer.isEmpty()) {
            return;
        }
        volunteer.get().getActivities().remove(result);
        userRepository.save(volunteer.get());
        result.getApplicants().remove(username);
        activityRepository.save(result);

        String message = "You got denied for the following activity \"" + result.getTitle() + "\" by " + result.getCreator();
        Notification notification = new Notification(result.getCreator(), message);
        notificationService.saveNotification(notification, username);
    }
}
