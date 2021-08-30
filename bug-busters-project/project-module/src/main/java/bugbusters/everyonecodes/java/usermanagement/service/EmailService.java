package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationDTO;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@EnableScheduling
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
    private final NotificationService notificationService;

    private final Map<String, String> allowedUsers = new HashMap<>();

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper, NotificationService notificationService) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
        this.notificationService = notificationService;
    }


    // send email with link that allows password change
    public void sendMail(String email) {
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) throw new IllegalArgumentException();
        var uuid = UUID.randomUUID().toString();

        // add the user and the uuid to map that allows password change
        allowedUsers.put(oUser.get().getUsername(), uuid);

        var subject = "Reset your Password";
        var message = "Please use this dummy link to create a new password:\n https://www.bugbusterseveryonecodes.com/passwordreset/" + uuid;

        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailMessage.setFrom("bugbusters21@gmail.com");

        javaMailSender.send(mailMessage);
    }


    // use the link sent by mail to actually set a new password
    public UserPrivateDTO savePassword(String email, String uuid, String newPassword) throws IllegalArgumentException {

        // check if user with that email exists
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) throw new IllegalArgumentException();
        var userTemp = oUser.get();


        // check if these values have been added to map by sendMail() method
        if (!allowedUsers.containsKey(userTemp.getUsername()) || !allowedUsers.get(userTemp.getUsername()).equals(uuid)) throw new IllegalArgumentException();

        // validate and save new password
        if (!newPassword.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!?@#$^&+=/_-])(?=\\S+$).{6,100}")) throw new IllegalArgumentException();
        userTemp.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userTemp);

        var userDTO = userDTOMapper.toUserPrivateDTO(userTemp);

        // remove used entry from map
        allowedUsers.remove(userTemp.getUsername());

        return userDTO;
    }

// clear map every day at 3am
    @Scheduled(cron= "0 0 3 * * *")
    public void clearMap() {
        allowedUsers.clear();
    }

// just for testing
    public void addEntryToMap(String key, String value) {
        allowedUsers.put(key, value);
    }


    //-----------email notifications-------------------

    //every day at 10am
    @Scheduled(cron= "0 0 10 * * ?")
    public void sendEmailNotificationDaily() {
        var subject = "Daily Notifications from BugBusters";
        var message = "Here are your notifications:\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.DAILY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            List<NotificationDTO> result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
                    .map(notificationService::convertToDTO)
                    .collect(Collectors.toList());
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message + result);
            mailMessage.setFrom("bugbusters21@gmail.com");
            javaMailSender.send(mailMessage);
        }
    }

    //every monday at 10am - weekly
    @Scheduled(cron= "0 0 10 ? * MON")
    public void sendEmailNotificationWeekly() {
        var subject = "Weekly Notifications from BugBusters";
        var message = "Here are your notifications:\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.WEEKLY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            List<NotificationDTO> result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(notificationService::convertToDTO)
                    .collect(Collectors.toList());
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message + result);
            mailMessage.setFrom("bugbusters21@gmail.com");
            javaMailSender.send(mailMessage);
        }
    }

    //on the first day of every month at 10am - monthly
    @Scheduled(cron= "0 0 10 1 * ?")
    public void sendEmailNotificationMonthly() {
        var subject = "Monthly Notifications from BugBusters";
        var message = "Here are your notifications:\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.MONTHLY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            List<NotificationDTO> result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusMonths(1)))
                    .map(notificationService::convertToDTO)
                    .collect(Collectors.toList());
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message + result);
            mailMessage.setFrom("bugbusters21@gmail.com");
            javaMailSender.send(mailMessage);
        }
    }

    //this method is only here to test the logic
    public List<List<NotificationDTO>> sendEmailNotificationDaily_forTesting() {
        var subject = "Monthly Notifications from BugBusters";
        var message = "Here are your notifications:\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.DAILY);
        List<List<NotificationDTO>> result = new ArrayList<>();
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            List<NotificationDTO> resultDaily = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
                    .map(notificationService::convertToDTO)
                    .collect(Collectors.toList());
            result.add(resultDaily);
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message + resultDaily);
            mailMessage.setFrom("bugbusters21@gmail.com");
            javaMailSender.send(mailMessage);
        }
        return result;
    }

    public String registerEmailNotification(String username, EmailSchedule schedule) {
        Optional<User> oUser = userRepository.findOneByUsername(username);
        if (oUser.isEmpty()) return "Oops, something went wrong.";
        User user = oUser.get();
        user.setEmailSchedule(schedule);
        userRepository.save(user);
        return username + " is registered for " + schedule.toString().toLowerCase() + " email notification";
    }
}