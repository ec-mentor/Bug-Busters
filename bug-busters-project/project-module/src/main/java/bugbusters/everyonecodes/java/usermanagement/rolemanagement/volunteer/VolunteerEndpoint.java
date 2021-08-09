package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/volunteer")
@Secured("ROLE_VOLUNTEER")
public class VolunteerEndpoint {
    
    private final VolunteerService volunteerService;

    public VolunteerEndpoint(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @GetMapping("/login")
    VolunteerPrivateDTO login(Authentication authentication) {
        return volunteerService.viewVolunteerPrivateData(authentication.getName()).orElse(null);
    }

    @PutMapping("/edit")
    VolunteerPrivateDTO editUserProfile(@Valid @RequestBody VolunteerPrivateDTO edits, Authentication authentication) {
        return volunteerService.editProfile(edits, authentication.getName()).orElse(null);
    }

    @GetMapping("/view")
    VolunteerPublicDTO viewOwnProfile(Authentication authentication) {
        return volunteerService.viewOwnProfile(authentication.getName()).orElse(null);
    }

    @GetMapping("/view/{username}")
    ClientPublicDTO viewClientProfile(@PathVariable String username) {
        return volunteerService.viewClientProfile(username).orElse(null);
    }
}
