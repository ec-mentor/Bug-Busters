package bugbusters.everyonecodes.java.activities;

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

    public ActivityService(ActivityRepository activityRepository, SetToStringMapper setToStringMapper, UserRepository userRepository) {
        this.activityRepository = activityRepository;
        this.setToStringMapper = setToStringMapper;
        this.userRepository = userRepository;
    }

    public Activity saveNewActivity(Activity activity, String username){
        activity.setStatusVolunteer(activity.getStatusClient());
        activity = activityRepository.save(activity);
        var oCreator = userRepository.findOneByUsername(username);
        if (oCreator.isPresent()) {
            var creator = oCreator.get();
            creator.getActivities().add(activity);
            userRepository.save(creator);
        }
        return activity;
    }


    public Optional<Activity> postDraft(Long id){
        var oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()){
            return Optional.empty();
        }
        Activity result = oActivity.get();
        result.setStatusClient(Status.PENDING);
        result.setStatusVolunteer(Status.PENDING);
        return Optional.of(activityRepository.save(result));
    }


    public List<Activity> findAll(){
        return activityRepository.findAll();
    }

    public Optional<Activity> edit(ActivityEditDTO input, Long id){
        Optional<Activity> oActivity = activityRepository.findById(id);
        if (oActivity.isEmpty()){
            return Optional.empty();
        }
        Activity result = oActivity.get();
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
}
