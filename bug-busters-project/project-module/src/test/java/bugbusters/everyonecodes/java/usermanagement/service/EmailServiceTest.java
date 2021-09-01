package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin.AdminRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class EmailServiceTest {
    @Autowired
    EmailService emailService;

    @MockBean
    JavaMailSender javaMailSender;

    @MockBean
    UserRepository userRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    UserDTOMapper userDTOMapper;

    @MockBean
    AdminRunner adminRunner;

    @MockBean
    NotificationService notificationService;

    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final Map<String, String> allowedUsers = Map.of("test", "test");

    @Test
    void sendMail() {
        Mockito.when(userRepository.findOneByEmail("test")).thenReturn(Optional.of(user));
        emailService.sendMail("test");
        Mockito.verify(javaMailSender).send(ArgumentMatchers.any(SimpleMailMessage.class));
    }

    @Test
    void savePassword() {
        Mockito.when(userRepository.findOneByEmail("test")).thenReturn(Optional.of(user));
        emailService.addEntryToMap("test", "test");
        emailService.savePassword("test", "test", "Coding12#");
        Mockito.verify(passwordEncoder).encode("Coding12#");
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }

    @Test
    void savePassword_WrongEmail() {
        Mockito.when(userRepository.findOneByEmail("test")).thenReturn(Optional.of(user));
        emailService.addEntryToMap("testFAIL", "test");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.savePassword("test", "test", "Coding12#");
        });
    }

    @Test
    void savePassword_WrongUUID() {
        Mockito.when(userRepository.findOneByEmail("test")).thenReturn(Optional.of(user));
        emailService.addEntryToMap("test", "testFAIL");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.savePassword("test", "test", "Coding12#");
        });
    }

    @Test
    void savePassword_WeakPassword() {
        Mockito.when(userRepository.findOneByEmail("test")).thenReturn(Optional.of(user));
        emailService.addEntryToMap("test", "test");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.savePassword("test", "test", "weak");
        });
    }

    //TODO: sendListOfActivityMailForKeyword() Test

    @Test
    void sendListOfActivityMailForKeyword() {

    }

}