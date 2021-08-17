package bugbusters.everyonecodes.java.activities;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/activities")
public class ActivityEndpoint {
    private final ActivityService activityService;

    public ActivityEndpoint(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/create/new")
    Activity saveNewActivity(@Valid @RequestBody Activity activity, Authentication authentication){
        return activityService.saveNewActivity(activity, authentication.getName());
    }

    @PutMapping("/post/{id}")
    Activity postDraft(@PathVariable Long id) {
        return activityService.postDraft(id).orElse(null);
    }

    @PutMapping("/edit/{id}")
    Activity editActivity(@Valid @RequestBody ActivityEditDTO input, @PathVariable Long id){
        return activityService.edit(input, id).orElse(null);
    }

    @GetMapping()
    List<Activity> getAllActivities(){
        return activityService.findAll();
    }



}
