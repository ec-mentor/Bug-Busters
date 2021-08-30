package bugbusters.everyonecodes.java.usermanagement.endpoints;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.service.EmailService;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserEndpoint {

    private final UserService userService;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public UserEndpoint(UserService userService, EmailService emailService, NotificationService notificationService) {
        this.userService = userService;
        this.emailService = emailService;
        this.notificationService = notificationService;
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

    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    @GetMapping("/notifications")
    List<Notification> getNotifications(Authentication authentication){
        return notificationService.findAllNotificationsChronologicalByUsername(authentication.getName());
    }


    @Secured({"ROLE_VOLUNTEER", "ROLE_INDIVIDUAL", "ROLE_ORGANIZATION"})
    @PutMapping("/notifications/email/{schedule}")
    String registerEmailNotifications(Authentication authentication, @PathVariable String schedule) {
        return emailService.registerEmailNotification(authentication.getName(), EmailSchedule.valueOf(schedule.toUpperCase()));
    }

    //endpoint only for review
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/notifications/email/test/{username}")
    void sendTestEmail(@PathVariable String username) {
        emailService.sendTestEmailNotification(username);
    }



}