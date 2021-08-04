package bugbusters.everyonecodes.java.profile.userpublic;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class UserPublicDTOEndpoint {
    private final UserPublicDTOService userPublicDTOService;

    public UserPublicDTOEndpoint(UserPublicDTOService userPublicDTOService) {
        this.userPublicDTOService = userPublicDTOService;
    }

    @GetMapping("/{username}")
    UserPublicDTO getDTOByUsername(@PathVariable String username){
        return userPublicDTOService.viewUserPublicDTO(username);
    }
}
