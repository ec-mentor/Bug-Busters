package bugbusters.everyonecodes.java.registration.endpoints;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserEndpoint {
    private final UserService userService;

    public UserEndpoint(UserService userService) {
        this.userService = userService;
    }
        // falls nötig namen der klassen und methoden ändern

    @PostMapping("/register")
    User registerUser(@RequestBody User user){
        return userService.saveUser(user);
    }
}
