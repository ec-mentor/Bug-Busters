package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/volunteer")
@Secured("ROLE_VOLUNTEER")
public class VolunteerEndpoint {

    private final VolunteerService volunteerService;

    public VolunteerEndpoint(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/login")
    VolunteerPrivateDTO viewVolunteerPrivateData(Authentication authentication) {
        return volunteerService.viewVolunteerPrivateData(authentication.getName()).orElse(null);
    }

    @PutMapping("/edit")
    VolunteerPrivateDTO editVolunteerData(@Valid @RequestBody VolunteerPrivateDTO edits, Authentication authentication) {
        return volunteerService.editVolunteerData(edits, authentication.getName()).orElse(null);
    }

    @GetMapping("/view")
    VolunteerPublicDTO viewVolunteerPublicData(Authentication authentication) {
        return volunteerService.viewVolunteerPublicData(authentication.getName()).orElse(null);
    }

    @GetMapping("/view/{username}")
    ClientPublicDTO viewClientPublicData(@PathVariable String username) {
        return volunteerService.viewClientPublicData(username).orElse(null);
    }

    @GetMapping("/activities/view")
    List<ActivityDTO> listAllPendingActivities() {
        return volunteerService.listAllPendingActivities();
    }

    @GetMapping("/activities/search/{text}")
    List<ActivityDTO> searchActivityByText(@PathVariable String text) {
        return volunteerService.searchPendingActivitiesByText(text);
    }

    @GetMapping("/activities/list")
    List<ActivityDTO> listAllOfUsersActivities(Authentication authentication) {
        return volunteerService.listAllActivitiesOfVolunteer(authentication.getName());
    }

    @GetMapping("/webapptree")
    String viewWebAppTree(@Value("${webapptree.volunteer}") String input) {
        return input;
    }
}