package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.File;
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

    // send Email with List of Activities
    public void sendListOfActivityMailForKeyword(String to, String keyword, String message, String subject) {
        var mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
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
        var message = "Here are your notifications:\n\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.DAILY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            String result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusHours(24)))
                    .map(this::toEmailString)
                    .collect(Collectors.joining("\n"));
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
        var message = "Here are your notifications:\n\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.WEEKLY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            String result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusDays(7)))
                    .map(this::toEmailString)
                    .collect(Collectors.joining("\n"));
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
        var message = "Here are your notifications:\n\n";
        var mailMessage = new SimpleMailMessage();
        List<User> users = userRepository.findByEmailSchedule(EmailSchedule.MONTHLY);
        for (User user : users) {
            List<Notification> notifications = user.getNotifications();
            if (notifications.isEmpty()) {
                continue;
            }
            String result = notifications.stream()
                    .filter(n -> n.getTimestamp().isAfter(LocalDateTime.now().minusMonths(1)))
                    .map(this::toEmailString)
                    .collect(Collectors.joining("\n"));
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject(subject);
            mailMessage.setText(message + result);
            mailMessage.setFrom("bugbusters21@gmail.com");
            javaMailSender.send(mailMessage);
        }
    }

    private String toEmailString(Notification notification) {
        return "From: \"" + notification.getCreator() + "\"\n" +
                "Message: \"" + notification.getMessage() + "\"\n";
    }

    //only for review to show test email for notifications
    public void sendTestHTMLEmail(String usernameInput) {
        Optional<User> oUser = userRepository.findOneByUsername(usernameInput);
        if (oUser.isEmpty()) return;
        String to = oUser.get().getEmail();
        String from = "bugbusters21@gmail.com";
        final String username = from;
        final String password = "kgatvfvoxznoyxsx";
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message,  true, "utf-8");
            helper.setTo(to);
            String body = "<h3><font color=black>Your Email notification is working!</font></h3><br>";
            body += "<font color=black><p><i>Here are your notifications:</i><br><br>";
            List<Notification> notifications = oUser.get().getNotifications();
            String notificationsAsString = notifications.stream()
                    .map(this::toEmailStringHTML)
                    .collect(Collectors.joining("<br>"));
            body += notificationsAsString;
            body += "<br><br>" + "Click on Link to unsubscribe: "
                    + "http://localhost:8080/users/notifications/email/unsubscribe/" + usernameInput + "</p></font>";
            helper.setText(body, true);
            helper.setSubject("Test Notifications from BugBusters");
            helper.setFrom(from);
            File file = new File("bug-busters-project/project-module/src/main/resources/BugBustersSmall.png");
            helper.addAttachment("BugBustersSmall.png", file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String toEmailStringHTML(Notification notification) {
        return "<b>From:</b> \"" + notification.getCreator() + "\"<br>" +
                "<b>Message:</b> \"" + notification.getMessage() + "\"<br>";
    }



    public String registerEmailNotification(String username, EmailSchedule schedule) {
        Optional<User> oUser = userRepository.findOneByUsername(username);
        if (oUser.isEmpty()) return "Oops, something went wrong.";
        User user = oUser.get();
        if (!user.getEmailSchedule().equals(EmailSchedule.NONE)) {
            return username + " already has a "
                    + schedule.toString().toLowerCase() + " notification subscription.";
        }
        user.setEmailSchedule(schedule);
        userRepository.save(user);
        return username + " is registered for " + schedule.toString().toLowerCase() + " email notification";
    }

    public String unsubscribeEmailNotification(String username) {
        Optional<User> oUser = userRepository.findOneByUsername(username);
        if (oUser.isEmpty()) return "Oops, something went wrong.";
        User user = oUser.get();
        user.setEmailSchedule(EmailSchedule.NONE);
        userRepository.save(user);
        return "You have successfully unsubscribed from your email notifications!";
    }
}