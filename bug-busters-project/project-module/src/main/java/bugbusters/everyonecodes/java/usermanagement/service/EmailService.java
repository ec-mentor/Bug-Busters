package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@EnableScheduling
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    private final List<User> users = new ArrayList<>();
    private final List<String> uuids = new ArrayList<>();

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
    }


    // send email with link that allows password change
    public void sendMail(String email) {
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) return;
        var uuid = UUID.randomUUID().toString();

        // add the user and the uuid to lists that allow password change
        users.add(oUser.get());
        uuids.add(uuid);

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
    public UserPrivateDTO savePassword(String email, String uuid, String newPassword) {

        // check if user with that email exists
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) return null;
        var userTemp = oUser.get();


        // check if these values have been added to lists by creating reset link and calling sendMail()
        if (!uuids.contains(uuid) || !users.contains(userTemp)) return null;


        // save new password
        userTemp.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userTemp);

        var userDTO = userDTOMapper.toUserPrivateDTO(userTemp);

        // remove used entries from lists
        uuids.remove(uuid);
        users.remove(userTemp);

        return userDTO;
    }

// clear lists every day at 3am

    @Scheduled(cron= "0 0 3 * * *")
    public void clearLists() {
        users.clear();
        uuids.clear();
    }
}