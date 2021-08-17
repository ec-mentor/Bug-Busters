package bugbusters.everyonecodes.java.activities;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;

import java.util.List;
import java.util.Optional;

public class ActivityService {
    private final ActivityRepository activityRepository;
    private final SetToStringMapper setToStringMapper;

    public ActivityService(ActivityRepository activityRepository, SetToStringMapper setToStringMapper) {
        this.activityRepository = activityRepository;
        this.setToStringMapper = setToStringMapper;
    }

    public Activity saveNewActivity(Activity activity){
        activity.setStatusVolunteer(activity.getStatusClient());
        return activityRepository.save(activity);
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
