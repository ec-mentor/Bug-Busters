package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.service.EmailService;
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
    private final EmailService emailService;

    public UserEndpoint(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
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

    @GetMapping("/passwordreset/{email}")
    void forgotPassword(@PathVariable String email) {
        emailService.sendMail(email);
    }

    @PostMapping("/passwordreset/{email}/{uuid}")
    UserPrivateDTO setPassword(@PathVariable String email, @PathVariable String uuid, @RequestBody String password) {
        return emailService.savePassword(email, uuid, password);
    }
}