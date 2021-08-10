package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/organization")
@Secured("ROLE_ORGANIZATION")
public class OrganizationEndpoint {

    private final OrganizationService organizationService;

    public OrganizationEndpoint(OrganizationService organizationService) {
        this.organizationService = organizationService;
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
}
