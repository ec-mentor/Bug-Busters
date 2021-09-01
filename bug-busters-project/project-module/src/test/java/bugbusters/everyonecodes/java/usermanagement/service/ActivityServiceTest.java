package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin.AdminRunner;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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

    @MockBean
    LocalDateNowProvider localDateNowProvider;

    @MockBean
    AdminRunner adminRunner;

    // for testing
    private final Long id = 1L;
    private final String username = "test";
    private final String username2 = "test2";
    private User user = new User(username, "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private Notification notification1 = new Notification("note1","some message");
    private Notification notification2 = new Notification("note2","some message");
    private Activity activity = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now().plusYears(1), false, Status.PENDING, Status.PENDING, null, null, null, null);
    private ActivityDTO activityDTO = new ActivityDTO("testDTO", "testDTO", "testDTO", Status.PENDING, LocalDateTime.now(), LocalDateTime.now().plusYears(1), null, null, null, null, null, null);
    private ActivityInputDTO activityInputDTO = new ActivityInputDTO("testInputDTO", "testInputDTO", "testInputDTO", null, LocalDateTime.now(), LocalDateTime.now().plusYears(1), false, Status.PENDING);

    @BeforeEach
    void setUp() {
        activity = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now().plusYears(1), false, Status.PENDING, Status.PENDING, null, null, null, null);
        activityDTO = new ActivityDTO("testDTO", "testDTO", "testDTO", Status.PENDING, LocalDateTime.now(), LocalDateTime.now().plusYears(1), null, null, null, null, null, null);
        activityInputDTO = new ActivityInputDTO("testInputDTO", "testInputDTO", "testInputDTO", null, LocalDateTime.now(), LocalDateTime.now().plusYears(1), false, Status.PENDING);
        notification1 = new Notification("test","some message");
        notification2 = new Notification("note2","some message");
        user = new User(username, "test", "test",
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
    void postDraft_ActivityNotFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.postDraft(id, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void postDraft_AuthenticationNameDoesNotMatch() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.postDraft(id, "wrongName");
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void postDraft_success() {
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(activityDTOMapper.toClientActivityDTO(any(Activity.class))).thenReturn(activityDTO);
        activityService.postDraft(id, username);
        Assertions.assertEquals(Status.PENDING, activity.getStatusClient());
        Assertions.assertEquals(Status.PENDING, activity.getStatusVolunteer());
        verify(activityRepository, times(1)).save(activity);
    }


    //edit activity
    @Test
    void edit_ActivityNotFound() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.edit(activityInputDTO, id, username2);
        verify(activityRepository, never()).save(any(Activity.class));
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void edit_WrongUser() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.edit(activityInputDTO, id, "wrongCreator");
        verify(activityRepository, never()).save(any(Activity.class));
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void edit_success() {
        activity.setCreator(username2);
        activity.setApplicants(List.of("test"));
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(activityDTOMapper.toClientActivityDTO(any(Activity.class))).thenReturn(activityDTO);
        activityService.edit(activityInputDTO, id, username2);
        verify(notificationService).saveNotification(any(Notification.class), any(String.class));
        verify(activityRepository, times(1)).save(any(Activity.class));
        Assertions.assertEquals(activityInputDTO.getDescription(), activity.getDescription());
    }

    @Test
    void delete_ActivityNotFound() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.delete(id, username2);
        verify(activityRepository, never()).delete(any(Activity.class));
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void delete_WrongUser() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.delete(id, "wrongCreator");
        verify(activityRepository, never()).delete(any(Activity.class));
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
    }

    @Test
    void delete_success() {
        activity.setCreator(username);
        activity.setApplicants(List.of("test2"));
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        activityService.delete(id, username);
        verify(userRepository).findOneByUsername(username);
        verify(notificationService).saveNotification(any(Notification.class), any(String.class));
        verify(activityRepository, times(1)).delete(any(Activity.class));
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
    void applyForActivity_ActivityNotFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.applyForActivity(id, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void applyForActivity_EndTimeInThePast() {
        activity.setEndTime(LocalDateTime.of(1999, 1, 1, 0, 0));
        when(localDateNowProvider.getLocalDateTimeNow()).thenReturn(LocalDateTime.now());
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.applyForActivity(id, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void applyForActivity_StatusNotPending() {
        activity.setStatusVolunteer(Status.IN_PROGRESS);
        when(localDateNowProvider.getLocalDateTimeNow()).thenReturn(LocalDateTime.now());
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.applyForActivity(id, username);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void applyForActivity_Success() {
        LocalDateTime localDateTime = LocalDateTime.now();
        when(localDateNowProvider.getLocalDateTimeNow()).thenReturn(LocalDateTime.now());
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        activityService.applyForActivity(id, username);
        verify(activityRepository, times(1)).save(any(Activity.class));
        verify(userRepository, times(1)).save(any(User.class));
        Assertions.assertTrue(activity.getApplicants().contains(username));
    }


    //approve application as a client
    @Test
    void approveApplicationAsClient_NoActivityFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.approveApplicationAsClient(id, username, username2);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void approveApplicationAsClient_wrongAuthName() {
        activity.setCreator(username2);
        activity.getApplicants().add(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.approveApplicationAsClient(id, username, "wrongCreator");
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void approveApplicationAsClient_VolunteerNotApplied() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.approveApplicationAsClient(id, username, username2);
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void approveApplicationAsClient_success() {
        activity.setCreator(username2);
        activity.getApplicants().add(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.approveApplicationAsClient(id, username, username2);
        Assertions.assertFalse(activity.getApplicants().contains(username));
        verify(activityRepository, times(1)).save(any(Activity.class));
    }


    //deny application as a client
    @Test
    void denyApplicationAsClient_NoActivityFound() {
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        activityService.denyApplicationAsClient(id, username, username2);
        verify(userRepository, never()).findOneByUsername(any(String.class));
    }

    @Test
    void denyApplicationAsClient_wrongAuthName() {
        activity.setCreator(username2);
        activity.getApplicants().add(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.denyApplicationAsClient(id, username, "wrongCreator");
        verify(userRepository, never()).findOneByUsername(any(String.class));
    }

    @Test
    void denyApplicationAsClient_VolunteerNotApplied() {
        activity.setCreator(username2);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        activityService.denyApplicationAsClient(id, username, username2);
        verify(userRepository, never()).findOneByUsername(any(String.class));
    }

    @Test
    void denyApplicationAsClient_VolunteerNotFound() {
        activity.setCreator(username2);
        activity.getApplicants().add(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.empty());
        activityService.denyApplicationAsClient(id, username, username2);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void denyApplicationAsClient_success() {
        activity.setCreator(username2);
        activity.getApplicants().add(username);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        activityService.denyApplicationAsClient(id, username, username2);
        verify(userRepository, times(1)).save(user);
        Assertions.assertFalse(activity.getApplicants().contains(username));
        verify(activityRepository, times(1)).save(activity);
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

    // delete an application as a volunteer
    @Test
    void deleteApplication_notFound() {
        activity.setVolunteer(username);
        when(activityRepository.findById(id)).thenReturn(Optional.empty());
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        activityService.deleteApplicationAsVolunteer(id, username);
        verify(notificationService, never()).saveNotification(any(Notification.class), any(String.class));
        verify(userRepository, never()).save(any(User.class));
        verify(activityRepository, never()).save(any(Activity.class));
    }

    @Test
    void deleteApplication_success() {
        activity.setVolunteer(username);
        user.getActivities().add(activity);
        when(activityRepository.findById(id)).thenReturn(Optional.of(activity));
        when(userRepository.findOneByUsername(username)).thenReturn(Optional.of(user));
        activityService.deleteApplicationAsVolunteer(id, username);
        verify(notificationService).saveNotification(any(Notification.class), any(String.class));
        verify(userRepository).save(any(User.class));
        verify(activityRepository).save(any(Activity.class));
    }
}
