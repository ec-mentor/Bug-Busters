package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ActivityServiceTest {

    @Autowired
    ActivityService activityService;

    @MockBean
    ActivityRepository activityRepository;

    @MockBean
    SetToStringMapper setToStringMapper;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ActivityDTOMapper activityDTOMapper;

    @MockBean
    NotificationService notificationService;

    // for testing
    private final Long id = 1L;
    private final String username = "test";
    private User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private Notification notification1 = new Notification("note1","some message");
    private Notification notification2 = new Notification("note2","some message");
    private Activity activity = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
    private ActivityDTO activityDTO = new ActivityDTO("test", "test", "test", Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
    private ActivityInputDTO activityInputDTO = new ActivityInputDTO("test", "test", "test", null, LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING);

    @BeforeEach
    void setUp() {
        activity = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
        activityDTO = new ActivityDTO("test", "test", "test", Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
        notification1 = new Notification("test","some message");
        notification2 = new Notification("note2","some message");
        user = new User("test", "test", "test",
                "test", LocalDate.of(2000, 1, 1), "test",
                "test", "test");
    }

    //save a new activity
    @Test
    void saveNewActivity_statusPendingAndUserFound() {
        activityInputDTO.setStatusClient(Status.PENDING);
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        when(activityDTOMapper.createNewActivityFromActivityInputDTO(activityInputDTO, username)).thenReturn(activity);
        when(activityRepository.save(activity)).thenReturn(activity);
        when(activityDTOMapper.toClientActivityDTO(activity)).thenReturn(activityDTO);
        activityService.saveNewActivity(activityInputDTO, username);
        verify(activityDTOMapper, times(1)).createNewActivityFromActivityInputDTO(activityInputDTO, username);
        verify(activityRepository, times(1)).save(any(Activity.class));
        Assertions.assertEquals(1, user.getActivities().size());
    }

    @Test
    void saveNewActivity_statusDraftAndUserNotFound() {
        activityInputDTO.setStatusClient(Status.DRAFT);
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        activityService.saveNewActivity(activityInputDTO, username);
        verify(activityDTOMapper, times(1)).createNewActivityFromActivityInputDTO(activityInputDTO, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void saveNewActivity_statusOther() {
        activityInputDTO.setStatusClient(Status.IN_PROGRESS);
        activityService.saveNewActivity(activityInputDTO, username);
        verify(activityDTOMapper, never()).createNewActivityFromActivityInputDTO(activityInputDTO, username);
    }


    //post a draft
    @Test
    void postDraft() {

    }


    //edit activity
    @Test
    void edit() {

    }


    //complete an activity as a client and notify a volunteer
    @Test
    void completeActivityClientNotifyVolunteer() {

    }


    //complete an activity as a volunteer
    @Test
    void completeActivityVolunteer() {

    }


    //apply for an activity as a volunteer
    @Test
    void applyForActivity() {

    }


    //approve application as a client
    @Test
    void approveApplicationAsClient() {

    }


    //deny application as a client
    @Test
    void denyApplicationAsClient() {

    }


    //contact a volunteer for an activity
    @Test
    void contactVolunteerForActivity_ActivityNotFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.contactVolunteerForActivity(id, username, activity.getCreator());
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void contactVolunteerForActivity_wrongUsername() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.contactVolunteerForActivity(id, username, "wrongCreator");
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void contactVolunteerForActivity_success() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.contactVolunteerForActivity(id, username, activity.getCreator());
        verify(notificationService, times(1)).saveNotification(any(Notification.class), any(String.class));
    }


    //approve a recommendation as a volunteer
    @Test
    void approveRecommendationAsVolunteer_ActivityNotFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.approveRecommendationAsVolunteer(id, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void approveRecommendationAsVolunteer_wrongUsername() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.approveRecommendationAsVolunteer(id, "username");
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void approveRecommendationAsVolunteer_success() {
        activity.setVolunteer(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.approveRecommendationAsVolunteer(id, username);
        verify(activityRepository, times(1)).save(any(Activity.class));
    }


    //deny a recommendation as a volunteer
    @Test
    void denyRecommendationAsVolunteer_noActivityFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.denyRecommendationAsVolunteer(id, username);
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void denyRecommendationAsVolunteer_wrongUsername() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.denyRecommendationAsVolunteer(id, "wrongUsername");
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void denyRecommendationAsVolunteer_success() {
        activity.setVolunteer(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.denyRecommendationAsVolunteer(id, username);
        verify(notificationService, times(1)).saveNotification(any(Notification.class), any(String.class));
        Assertions.assertNull(activity.getVolunteer());
    }

}
