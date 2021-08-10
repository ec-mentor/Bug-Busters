package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    User registerUser(@Valid @RequestBody User user){
        try {
            return userService.saveUser(user);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid Password", e
            );
        }
    }
//
//    @GetMapping("/login")
//    UserPrivateDTO viewUserProfile(Authentication authentication) {
//        return userService.viewUserPrivateData(authentication.getName()).orElse(null);
//    }
//
//    @PutMapping("/edit")
//    UserPrivateDTO editUserProfile(@Valid @RequestBody UserPrivateDTO edits, Authentication authentication) {
//        return userService.editUserData(edits, authentication.getName()).orElse(null);
//    }
//
//    @GetMapping("/view")
//    UserPublicDTO viewUserPublicData(@RequestParam(name="name", required = false) String name, Authentication authentication) {
//        if (name == null) name = authentication.getName();
//        return userService.viewUserPublicData(name).orElse(null);
//    }
}
