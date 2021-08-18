package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityInputDTO;
import bugbusters.everyonecodes.java.activities.ActivityService;
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
@RequestMapping("/organization")
@Secured("ROLE_ORGANIZATION")
public class OrganizationEndpoint {

    private final OrganizationService organizationService;
    private final ActivityService activityService;

    public OrganizationEndpoint(OrganizationService organizationService, ActivityService activityService) {
        this.organizationService = organizationService;
        this.activityService = activityService;
    }

    @GetMapping("/login")
    ClientPrivateDTO viewOrganizationPrivateData(Authentication authentication) {
        return organizationService.viewOrganisationPrivateData(authentication.getName()).orElse(null);
    }

    @PutMapping("/edit")
    ClientPrivateDTO editOrganizationData(@Valid @RequestBody ClientPrivateDTO edits, Authentication authentication) {
        return organizationService.editOrganizationData(edits, authentication.getName()).orElse(null);
    }

    @GetMapping("/view")
    ClientPublicDTO viewOrganizationPublicData(Authentication authentication) {
        return organizationService.viewOrganisationPublicData(authentication.getName()).orElse(null);
    }

    @GetMapping("/view/{username}")
    VolunteerPublicDTO viewVolunteerPublicData(@PathVariable String username) {
        return organizationService.viewVolunteerPublicData(username).orElse(null);
    }

    @GetMapping("/view/volunteers")
    List<VolunteerSearchResultDTO> listAllVolunteers() {
        return organizationService.listAllVolunteers();
    }

    @GetMapping("/search/volunteers/{text}")
    ResponseEntity<Object> searchVolunteersByText(@PathVariable String text) {
        var searchResult = organizationService.searchVolunteersByText(text);
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
        return organizationService.listAllActivitiesOfOrganization(authentication.getName());
    }

    @GetMapping("/activities/list/drafts")
    List<ActivityDTO> listAllOfUsersDrafts(Authentication authentication) {
        return organizationService.listAllDraftsOfOrganization(authentication.getName());
    }

    @GetMapping("/webapptree")
    String viewWebAppTree(@Value("${webapptree.organization}") String input) {
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
