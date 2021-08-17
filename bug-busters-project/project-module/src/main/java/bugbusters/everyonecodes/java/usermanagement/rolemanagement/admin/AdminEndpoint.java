package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminEndpoint {

    private final AdminService service;

    public AdminEndpoint(AdminService service) {
        this.service = service;
    }

    @GetMapping("/volunteers")
    List<AdminDTO> getVolunteers() {
        return service.listAllVolunteers();
    }

    @GetMapping("/organizations")
    List<AdminDTO> getOrganizations() {
        return service.listAllOrganizations();
    }

    @GetMapping("/individuals")
    List<AdminDTO> getIndividuals() {
        return service.listAllIndividuals();
    }

    @GetMapping("/account/{username}")
    Object getAccount(@PathVariable String username) {
        return service.getAccountDataByUsername(username);
    }

}
