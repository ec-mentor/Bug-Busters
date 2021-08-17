package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    private User userTemp = null;

    public EmailService(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, UserDTOMapper userDTOMapper) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDTOMapper = userDTOMapper;
    }

    public void sendMail(String email) {
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty()) return;
        userTemp = oUser.get();

        var subject = "Reset your Password";
        var message = "Please click this dummy link to create a new password:\nhttps://www.w3.org/Provider/Style/dummy.html";

        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom("bugbusters21@gmail.com");

        javaMailSender.send(mailMessage);
    }

    public UserPrivateDTO savePassword(String email, String newPassword) {
        var oUser = userRepository.findOneByEmail(email);
        if (oUser.isEmpty() || userTemp == null) return null;

        userTemp.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userTemp);
        var userDTO = userDTOMapper.toUserPrivateDTO(userTemp);
        userTemp = null;
        return userDTO;
    }
}