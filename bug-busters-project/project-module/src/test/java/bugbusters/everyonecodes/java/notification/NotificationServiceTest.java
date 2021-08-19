package bugbusters.everyonecodes.java.notification;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin.AdminRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockitoPostProcessor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NotificationServiceTest {
    @Autowired
    NotificationService notificationService;

    @MockBean
    NotificationRepository notificationRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    AdminRunner adminRunner;

    private final String username = "test";
    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final Notification notification1 = new Notification("note1","some message");
    private final Notification notification2 = new Notification("note2","some message");

    @Test
    void findAllNotificationsChronologicalByUsername() {
        notification1.setTimestamp(LocalDateTime.of(1990, 1, 1, 1, 1));
        notification2.setTimestamp(LocalDateTime.of(1989, 1, 1, 1, 1));
        user.setNotifications(List.of(notification1, notification2));
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        var result = notificationService.findAllNotificationsChronologicalByUsername(username);
        Assertions.assertEquals(List.of(notification1, notification2), result);
    }

    @Test
    void saveNotification() {
        Mockito.when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        notificationService.saveNotification(notification1, username);
        Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
    }
}