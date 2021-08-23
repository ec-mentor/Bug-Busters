package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityService;
import bugbusters.everyonecodes.java.search.FilterActivity;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final ActivityService activityService;

    public VolunteerEndpoint(VolunteerService volunteerService, ActivityService activityService) {
        this.volunteerService = volunteerService;
        this.activityService = activityService;
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

    @GetMapping("/activities/list/pending")
    List<ActivityDTO> listAllPendingActivities() {
        return volunteerService.listAllPendingActivities();
    }

    @GetMapping("/activities/search/{text}")
    ResponseEntity<Object> searchActivitiesByText(@PathVariable String text) {
        var searchResult = volunteerService.searchPendingActivitiesByText(text);
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>("No results found for \"" + text + "\"", HttpStatus.OK);
        }
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @GetMapping("/activities/search/filtered/{text}")
    ResponseEntity<Object> searchActivitiesByTextFiltered(@PathVariable String text, @RequestBody FilterActivity filterActivity) {
        var searchResult = volunteerService.searchPendingActivitiesByTextFiltered(text, filterActivity);
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>("No results found for '" + text + "'", HttpStatus.OK);
        }
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @GetMapping("/activities/list")
    List<ActivityDTO> listAllOfUsersActivities(Authentication authentication) {
        return volunteerService.listAllActivitiesOfVolunteer(authentication.getName());
    }

    @GetMapping("/webapptree")
    String viewWebAppTree(@Value("${webapptree.volunteer}") String input) {
        return input;
    }

    @PutMapping("/activities/complete/{id}/{rating}")
    ActivityDTO completeActivityVolunteer(@PathVariable Long id, @PathVariable int rating, @RequestBody String feedback, Authentication authentication){
        return activityService.completeActivityVolunteer(id, rating, feedback, authentication.getName()).orElse(null);
    }

    @PutMapping("/activities/apply/{id}")
    void applyForActivity(@PathVariable Long id, Authentication authentication){
        activityService.applyForActivity(id, authentication.getName());
    }

    @PutMapping("/activities/approve/{id}")
    void approveApplication(@PathVariable Long id, Authentication authentication){
        activityService.approveRecommendationAsVolunteer(id, authentication.getName());
    }

    @PutMapping("/activities/deny/{id}")
    void denyApplication(@PathVariable Long id, Authentication authentication){
        activityService.denyRecommendationAsVolunteer(id, authentication.getName());
    }

}