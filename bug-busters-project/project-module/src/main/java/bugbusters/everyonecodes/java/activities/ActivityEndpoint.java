package bugbusters.everyonecodes.java.activities;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityEndpoint {
    private final ActivityService activityService;

    public ActivityEndpoint(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/post/new")
    Activity saveNewActivity(@RequestBody Activity activity){
        return activityService.saveNewActivity(activity);
    }

    @PutMapping("/post/{id}")
    Activity postDraft(@PathVariable Long id) {
        return activityService.postDraft(id).orElse(null);
    }

    @PutMapping("/edit/{id}")
    Activity editActivity(@RequestBody ActivityEditDTO input, @PathVariable Long id){
        return activityService.edit(input, id).orElse(null);
    }

    @GetMapping()
    List<Activity> getAllActivities(){
        return activityService.findAll();
    }



}
