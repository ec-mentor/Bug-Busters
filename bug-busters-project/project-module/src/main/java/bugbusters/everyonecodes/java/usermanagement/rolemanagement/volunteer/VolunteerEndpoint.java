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
}
