package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.SendFailedException;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String email) {

        var subject = "Reset your Password";
        var message = "Please click this dummy link to create a new password:\nhttps://www.w3.org/Provider/Style/dummy.html";

        var mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailMessage.setFrom("bugbusters21@gmail.com");

        javaMailSender.send(mailMessage);
    }
}