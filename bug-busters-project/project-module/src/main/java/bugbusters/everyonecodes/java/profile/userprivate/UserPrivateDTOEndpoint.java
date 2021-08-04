package bugbusters.everyonecodes.java.profile.userprivate;

import bugbusters.everyonecodes.java.registration.data.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/private")
public class UserPrivateDTOEndpoint {

    private final ProfileEditor profileEditor;
    private final UserPrivateDTOService userPrivateDTOService;

    public UserPrivateDTOEndpoint(ProfileEditor profileEditor, UserPrivateDTOService userPrivateDTOService) {
        this.profileEditor = profileEditor;
        this.userPrivateDTOService = userPrivateDTOService;
    }

    @GetMapping("/view")
    UserPrivateDTO viewPrivateUserProfile(Authentication authentication) {
        User user = profileEditor.getUser(authentication.getName());
        return userPrivateDTOService.toDTO(user);
    }

    @PutMapping("/edit")
    User editPrivateUserProfile(Authentication authentication) {
        User user = profileEditor.getUser(authentication.getName());
        return profileEditor.editUserData(userPrivateDTOService.toDTO(user));
    }

}
