package bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityInputDTO;
import bugbusters.everyonecodes.java.activities.ActivityService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerSearchResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/individual")
@Secured("ROLE_INDIVIDUAL")
public class IndividualEndpoint {
    private final IndividualService individualService;
    private final ActivityService activityService;

    public IndividualEndpoint(IndividualService individualService, ActivityService activityService) {
        this.individualService = individualService;
        this.activityService = activityService;
    }


    @GetMapping("/login")
    ClientPrivateDTO viewIndividualPrivateData(Authentication authentication) {
        return individualService.viewIndividualPrivateData(authentication.getName()).orElse(null);
    }

    @PutMapping("/edit")
    ClientPrivateDTO editIndividualData(@Valid @RequestBody ClientPrivateDTO edits, Authentication authentication) {
        return individualService.editIndividualData(edits, authentication.getName()).orElse(null);
    }

    @GetMapping("/view")
    ClientPublicDTO viewIndividualPublicData(Authentication authentication) {
        return individualService.viewIndividualPublicData(authentication.getName()).orElse(null);
    }

    @GetMapping("/view/{username}")
    VolunteerPublicDTO viewVolunteerPublicDat(@PathVariable String username) {
        return individualService.viewVolunteerPublicData(username).orElse(null);
    }

    @GetMapping("/view/volunteers")
    List<VolunteerSearchResultDTO> listAllVolunteers() {
        return individualService.listAllVolunteers();
    }

    @GetMapping("/search/volunteers/{text}")
    ResponseEntity<Object> searchVolunteersByText(@PathVariable String text) {
        var searchResult = individualService.searchVolunteersByText(text);
        if (searchResult.isEmpty()) {
            return new ResponseEntity<>("No results found for '" + text + "'", HttpStatus.OK);
        }
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }

    @PostMapping("/activities/create/new")
    Activity saveNewActivity(@Valid @RequestBody ActivityInputDTO activity, Authentication authentication){
        return activityService.saveNewActivity(activity, authentication.getName()).orElse(null);
    }

    @PutMapping("/activities/post/{id}")
    Activity postDraft(@PathVariable Long id) {
        return activityService.postDraft(id).orElse(null);
    }

    @PutMapping("/activities/edit/{id}")
    Activity editActivity(@Valid @RequestBody ActivityInputDTO input, @PathVariable Long id, Authentication authentication){
        return activityService.edit(input, id, authentication.getName()).orElse(null);
    }

    @GetMapping("/activities/list")
    List<ActivityDTO> listAllOfUsersActivities(Authentication authentication) {
        return individualService.listAllActivitiesOfIndividual(authentication.getName());
    }

    @GetMapping("/activities/list/drafts")
    List<ActivityDTO> listAllOfUsersDrafts(Authentication authentication) {
        return individualService.listAllDraftsOfIndividual(authentication.getName());
    }

    @GetMapping("/webapptree")
    String viewWebAppTree(@Value("${webapptree.individual}") String input) {
        return input;
    }

    @PutMapping("/activities/complete/{id}/{rating}")
    Activity completeActivityClientNotifyVolunteer(@PathVariable Long id, @PathVariable int rating, @RequestBody String feedback){
        return activityService.completeActivityClientNotifyVolunteer(id, rating, feedback).orElse(null);
    }

    @PostMapping("/activities/approve/{id}/{userame}")
    Activity approveApplication(@PathVariable Long id, @PathVariable String username){
        return activityService.approveApplication(id, username).orElse(null);
    }

    @PostMapping("/activities/deny/{id}/{userame}")
    void denyApplication(@PathVariable Long id, @PathVariable String username){
        activityService.denyApplication(id, username);
    }
}