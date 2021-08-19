package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.notification.Notification;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static java.time.ZoneOffset.UTC;

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
    private final String username = "test";
    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final Notification notification1 = new Notification("note1","some message");
    private final Notification notification2 = new Notification("note2","some message");

    private final Activity pending = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.PENDING, Status.PENDING, null, null, null, null);
    private final Activity draft = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.DRAFT, Status.DRAFT, null, null, null, null);
    private final Activity inProgress = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.IN_PROGRESS, Status.IN_PROGRESS, null, null, null, null);
    private final Activity completed = new Activity("test", "test", "test", null, null, LocalDateTime.now(), LocalDateTime.now(), false, Status.COMPLETED, Status.COMPLETED, null, null, null, null);

    private final ActivityDTO pendingDTO = new ActivityDTO("test", "test", "test", Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
    private final ActivityDTO draftDTO = new ActivityDTO("test", "test", "test", Status.DRAFT, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
    private final ActivityDTO inProgressDTO =  new ActivityDTO("test", "test", "test", Status.IN_PROGRESS, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
    private final ActivityDTO completedDTO =  new ActivityDTO("test", "test", "test", Status.COMPLETED, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);




    //save a new activity
    @Test
    void saveNewActivity() {

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
    void contactVolunteerForActivity() {

    }

    //approve a recommendation as a volunteer
    @Test
    void approveRecommendationAsVolunteer() {

    }

    //deny a recommendation as a volunteer
    @Test
    void denyRecommendationAsVolunteer() {

    }

    //remove an activity from applicants
    @Test
    void removeActivityFromApplicants() {

    }


}
