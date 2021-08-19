package bugbusters.everyonecodes.java.usermanagement.service;

import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.notification.NotificationService;
import bugbusters.everyonecodes.java.usermanagement.repository.UserRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.SetToStringMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
