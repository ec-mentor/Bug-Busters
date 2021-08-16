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

    @PostMapping("/post")
    Activity postActivity(@RequestBody Activity activity) {
        return activityService.postActivity(activity);
    }

    @PutMapping("/edit/{title}")
    Activity editActivity(@RequestBody Activity activity, @PathVariable String title){
        return activityService.edit(activity, title).orElse(null);
    }

    @GetMapping()
    List<Activity> getAllActivities(){
        return activityService.findAll();
    }

    @GetMapping("/{title}")
    Activity getByTitle(@PathVariable String title){
        return activityService.getActivityByTitle(title).orElse(null);
    }


}
