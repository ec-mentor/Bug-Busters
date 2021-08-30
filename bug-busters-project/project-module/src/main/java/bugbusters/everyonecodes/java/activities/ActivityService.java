package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import bugbusters.everyonecodes.java.usermanagement.service.LocalDateNowProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SetToStringMapper setToStringMapper;
    private final UserRepository userRepository;
    private final ActivityDTOMapper activityDTOMapper;
    private final NotificationService notificationService;
    private final LocalDateNowProvider localDateNowProvider;

    public ActivityService(ActivityRepository activityRepository, SetToStringMapper setToStringMapper, UserRepository userRepository, ActivityDTOMapper activityDTOMapper, NotificationService notificationService, LocalDateNowProvider localDateNowProvider) {
        this.activityRepository = activityRepository;
        this.setToStringMapper = setToStringMapper;
        this.userRepository = userRepository;
        this.activityDTOMapper = activityDTOMapper;
        this.notificationService = notificationService;
        this.localDateNowProvider = localDateNowProvider;
    }

    public Optional<ActivityDTO> saveNewActivity(ActivityInputDTO activityInputDTO, String creatorAuthName) {
        if (!activityInputDTO.getStatusClient().equals(Status.PENDING) && !activityInputDTO.getStatusClient().equals(Status.DRAFT)) return Optional.empty();
        Activity activity = activityDTOMapper.createNewActivityFromActivityInputDTO(activityInputDTO, creatorAuthName);
        if (Status.PENDING.equals(activity.getStatusClient())) {
            activity.setPostedDate(localDateNowProvider.getLocalDateTimeNow());
        }
        var oCreator = userRepository.findOneByUsername(creatorAuthName);
        if (oCreator.isPresent()) {
            activity = activityRepository.save(activity);
            var creator = oCreator.get();
            creator.getActivities().add(activity);
            userRepository.save(creator);
            return Optional.of(activityDTOMapper.toClientActivityDTO(activity));
        }
        return Optional.empty();
    }

    public Optional<ActivityDTO> postDraft(Long id, String creatorAuthName) {
        var oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oActivity.get();
        if (!creatorAuthName.equals(result.getCreator())) {
            return Optional.empty();
        }
        result.setStatusClient(Status.PENDING);
        result.setStatusVolunteer(Status.PENDING);
        result.setPostedDate(localDateNowProvider.getLocalDateTimeNow());
        activityRepository.save(result);
        return Optional.of(activityDTOMapper.toClientActivityDTO(result));
    }

    public Optional<ActivityDTO> edit(ActivityInputDTO input, Long id, String username) {
        Optional<Activity> oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oActivity.get();
        if (!result.getCreator().equals(username)) return Optional.empty();

        // check so it only works on drafts or pending activities
        if (!result.getStatusClient().equals(Status.DRAFT) && !result.getStatusClient().equals(Status.PENDING)) return Optional.empty();

        var title = result.getTitle();

        result.setTitle(input.getTitle());
        result.setDescription(input.getDescription());
        result.setRecommendedSkills(setToStringMapper.convertToSet(input.getRecommendedSkills()));
        result.setCategories(setToStringMapper.convertToSet(input.getCategories()));
        result.setStartTime(input.getStartTime());
        result.setEndTime(input.getEndTime());
        result.setOpenEnd(input.isOpenEnd());
        activityRepository.save(result);

        // send a notification to all applicants
        var applicants = result.getApplicants();
        for (String applicant: applicants) {
            notificationService.saveNotification(new Notification(username, "There have been changes to an activity you applied to: " + title), applicant);
        }

        return Optional.of(activityDTOMapper.toClientActivityDTO(result));
    }


    public void delete(Long id, String username) {
        var oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()) return;
        var activity = oActivity.get();

        if (!activity.getCreator().equals(username)) return;

        var oCreator = userRepository.findOneByUsername(activity.getCreator());
        if (oCreator.isEmpty()) return;
        var creator = oCreator.get();

        // check so it only works on drafts or pending activities
        if (!activity.getStatusClient().equals(Status.DRAFT) && !activity.getStatusClient().equals(Status.PENDING)) return;

        creator.getActivities().remove(activity);

        // send a notification to all applicants and remove from their activities
        var applicants = activity.getApplicants();
        for (String applicant: applicants) {
            var oUser = userRepository.findOneByUsername(applicant);
            oUser.ifPresent(user -> user.getActivities().remove(activity));
            notificationService.saveNotification(new Notification(username, "There following activity you applied to has been deleted: " + activity.getTitle()), applicant);
        }
        activityRepository.delete(activity);
    }

    public Optional<ActivityDTO> completeActivityClientNotifyVolunteer(Long id, int rating, String feedback, String creatorAuthName) {
        Optional<Activity> oResult = activityRepository.findById(id);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        if (!creatorAuthName.equals(result.getCreator())) {
            return Optional.empty();
        }
        String volunteerUsername = result.getVolunteer();
        var oUser = userRepository.findOneByUsername(volunteerUsername);
        if (oUser.isEmpty()) return Optional.empty();
        result.setStatusClient(Status.COMPLETED);
        result.setRatingFromClient(rating);
        oUser.get().getRatings().add(rating);
        userRepository.save(oUser.get());
        if (!feedback.isEmpty()) {
            result.setFeedbackFromClient(feedback);
        }
        String message = "Congratulations, you have completed the activity: " + result.getTitle() + " from " + result.getCreator() + ". You have been rated: " + rating + ". To finish the process you have to rate the organization and you can give optional feedback!";
        Notification notification = new Notification(result.getCreator(), message);
        notificationService.saveNotification(notification, volunteerUsername);
        return Optional.of(activityDTOMapper.toClientActivityDTO(activityRepository.save(result)));
    }

    public Optional<ActivityDTO> completeActivityVolunteer(Long id, int rating, String feedback, String volunteerAuthName) {
        Optional<Activity> oResult = activityRepository.findById(id);
        if (oResult.isEmpty()) {
            return Optional.empty();
        }
        Activity result = oResult.get();
        if (!result.getStatusClient().equals(Status.COMPLETED) || !volunteerAuthName.equals(result.getVolunteer())) {
            return Optional.empty();
        }
        var oUser = userRepository.findOneByUsername(result.getCreator());
        if (oUser.isEmpty()) return Optional.empty();
        result.setStatusVolunteer(Status.COMPLETED);
        result.setRatingFromVolunteer(rating);
        oUser.get().getRatings().add(rating);
        userRepository.save(oUser.get());
        if (!feedback.isEmpty()) {
            result.setFeedbackFromVolunteer(feedback);
        }
        return Optional.of(activityDTOMapper.toVolunteerActivityDTO(activityRepository.save(result)));
    }

    public void applyForActivity(Long activityId, String username) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return;
        }
        Activity result = oResult.get();
        if (localDateNowProvider.getLocalDateTimeNow().isAfter(result.getEndTime())) {
            return;
        }
        if (!result.getStatusVolunteer().equals(Status.PENDING)) {
            return;
        }
        Optional<User> volunteer = userRepository.findOneByUsername(username);
        if (volunteer.isEmpty()) {
            return;
        }
        result.getApplicants().add(username);
        activityRepository.save(result);
        volunteer.get().getActivities().add(result);
        userRepository.save(volunteer.get());
        String message = username + " applied for your activity \"" + result.getTitle() + "\"!";
        Notification notification = new Notification(username, message);
        notificationService.saveNotification(notification, result.getCreator());
    }

    public void approveApplicationAsClient(Long activityId, String username, String creatorAuthName) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return;
        }
        Activity result = oResult.get();
        if (!creatorAuthName.equals(result.getCreator())) {
            return;
        }
        if (!result.getApplicants().contains(username)) {
            return;
        }
        result.setStatusVolunteer(Status.IN_PROGRESS);
        result.setStatusClient(Status.IN_PROGRESS);
        result.setVolunteer(username);
        result.getApplicants().remove(username);
        removeActivityFromApplicants(result);
        activityRepository.save(result);
    }

    public void denyApplicationAsClient(Long activityId, String username, String creatorAuthName) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return;
        }
        Activity result = oResult.get();
        if (!creatorAuthName.equals(result.getCreator())) {
            return;
        }
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

    public void contactVolunteerForActivity(Long activityId, String username, String creatorAuthName) {
        Optional<Activity> oResult = activityRepository.findById(activityId);
        if (oResult.isEmpty()) {
            return;
        }
        Activity result = oResult.get();
        if (!creatorAuthName.equals(result.getCreator())) {
            return;
        }
        result.setVolunteer(username);
        String message = result.getCreator() + " contacted you for the following activity: \"" + result.getTitle() + "\" (ID: " + result.getId() + "). Please approve or deny this activity!";
        Notification notification = new Notification(result.getCreator(), message);
        notificationService.saveNotification(notification, username);
    }

    public void approveRecommendationAsVolunteer(Long activityId, String username) {
        Optional<Activity> oActivity = activityRepository.findById(activityId);
        if (oActivity.isEmpty()) {
            return;
        }
        Activity activity = oActivity.get();
        if (!username.equals(activity.getVolunteer())) {
            return;
        }
        activity.setStatusVolunteer(Status.IN_PROGRESS);
        activity.setStatusClient(Status.IN_PROGRESS);
        activity.getApplicants().remove(username);
        removeActivityFromApplicants(activity);
        activityRepository.save(activity);
    }

    public void denyRecommendationAsVolunteer(Long activityId, String username) {
        Optional<Activity> oActivity = activityRepository.findById(activityId);
        if (oActivity.isEmpty()) {
            return;
        }
        Activity activity = oActivity.get();
        if (!username.equals(activity.getVolunteer())) {
            return;
        }
        activity.setVolunteer(null);
        String message = username + " has denied your activity recommendation for \"" + activity.getTitle() + "\"!";
        Notification notification = new Notification(activity.getCreator(), message);
        notificationService.saveNotification(notification, activity.getCreator());
    }

    private void removeActivityFromApplicants(Activity result) {
        result.getApplicants()
                .forEach(applicant -> {
                            var volunteer = userRepository.findOneByUsername(applicant);
                            volunteer.ifPresent(user -> {
                                user.getActivities().remove(result);
                                userRepository.save(volunteer.get());
                            });
                        }
                );
    }

}
