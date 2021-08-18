package bugbusters.everyonecodes.java.notification;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public List<Notification> findAllNotificationsChronological(){
        List<Notification> result = notificationRepository.findAll();
        return result.stream().sorted(Comparator.comparing(Notification::getTimestamp).reversed()).collect(Collectors.toList());
    }

    public void saveNotification(Notification notification, String username){
        Optional<User> oResult = userRepository.findOneByUsername(username);
        if (oResult.isEmpty() || notification == null){
            return;
        }
        User result = oResult.get();
        result.getNotifications().add(notification);
        userRepository.save(result);
    }
}
