package bugbusters.everyonecodes.java.activities;

import java.util.List;
import java.util.Optional;

public class ActivityService {
    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> findAll(){
        return activityRepository.findAll();
    }
    
    public Optional<Activity> edit(Activity input, String title){
        Optional<Activity> oActivity = activityRepository.getActivityByTitle(title);
        if (oActivity.isEmpty()){
            return Optional.empty();
        }
        Activity result = oActivity.get();
        result.setCreator(input.getCreator());
        result.setTitle(input.getTitle());
        result.setDescription(input.getDescription());
        result.setRecommendedSkills(input.getRecommendedSkills());
        result.setCategories(input.getCategories());
        result.setStartTime(input.getStartTime());
        result.setStartTime(input.getStartTime());
        result.setEndTime(input.getEndTime());
        result.setEndTime(input.getEndTime());
        result.setOpenEnd(input.isOpenEnd());
        return Optional.of(activityRepository.save(result));
    }
    public Optional<Activity> getActivityByTitle(String title){
        return activityRepository.getActivityByTitle(title);
    }

    public List<Activity> findAllPendingActivities() {
        return activityRepository.findAllByStatusClient(Status.PENDING);
    }
}
