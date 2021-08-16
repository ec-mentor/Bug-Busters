package bugbusters.everyonecodes.java.activities;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class ActivityEndpoint {


    @PostMapping
    Activity createActivity(@RequestBody Activity activity) {
        return activityService.save(activity);
    }

}
