package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.search.ActivityTextSearchService;
import bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.*;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static bugbusters.everyonecodes.java.usermanagement.data.EmailSchedule.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VolunteerServiceTest {

    @Autowired
    VolunteerService volunteerService;

    @MockBean
    VolunteerRepository volunteerRepository;

    @MockBean
    OrganizationRepository organizationRepository;

    @MockBean
    IndividualRepository individualRepository;

    @MockBean
    ActivityRepository activityRepository;

    @MockBean
    UserService userService;

    @MockBean
    ClientDTOMapper clientMapper;

    @MockBean
    VolunteerDTOMapper volunteerMapper;

    @MockBean
    ActivityTextSearchService activityTextSearchService;

    @MockBean
    ActivityDTOMapper activityDTOMapper;

    // for testing
    private final String username = "test";
    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final UserPrivateDTO userPrivateDTO = new UserPrivateDTO(username, user.getRole(), user.getFullName(), user.getBirthday(), user.getAddress(), user.getEmail(), user.getDescription());
    private final UserPublicDTO userPublicDTO = new UserPublicDTO(username, "test", 1, "test", 5.0);
    private final Individual individual = new Individual(user);
    private final Organization organization = new Organization(user);
    private final Volunteer volunteer = new Volunteer(user);
    private final Activity activity = new Activity("test", "test", "test", Set.of("test"), Set.of("test"), LocalDateTime.now(), LocalDateTime.now(), true, Status.PENDING, Status.PENDING, null, null, null, null);
    private final ActivityDTO activityDTO = new ActivityDTO("test", "test", "test", Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);
    private Object ActivityDTO;

    @Test
    void getVolunteerByUsername() {
        volunteerService.getVolunteerByUsername(username);
        Mockito.verify(volunteerRepository).findOneByUser_username(username);
    }

    @Test
    void viewVolunteerPrivateData_UserFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerPrivateDTO(volunteer)).thenReturn(new VolunteerPrivateDTO(userPrivateDTO, null));
        var oResult = volunteerService.viewVolunteerPrivateData(username);
        Assertions.assertEquals(Optional.of(new VolunteerPrivateDTO(userPrivateDTO, null)), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.times(1)).toVolunteerPrivateDTO(volunteer);
    }

    @Test
    void viewVolunteerPrivateData_UserNotFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = volunteerService.viewVolunteerPrivateData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.never()).toVolunteerPrivateDTO(Mockito.any(Volunteer.class));
    }

    @Test
    void viewVolunteerPublicData_UserFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerPublicDTO(volunteer)).thenReturn(new VolunteerPublicDTO(userPublicDTO, null));
        var oResult = volunteerService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.of(new VolunteerPublicDTO(userPublicDTO, null)), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.times(1)).toVolunteerPublicDTO(volunteer);
    }

    @Test
    void viewVolunteerPublicData_UserNotFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = volunteerService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.never()).toVolunteerPrivateDTO(Mockito.any(Volunteer.class));
    }

    @Test //this tests getClientByUsername private helper method as well
    void viewClientPublicData_OrganizationFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.of(organization));
        Mockito.when(clientMapper.toClientPublicDTO(organization)).thenReturn(new ClientPublicDTO(userPublicDTO));
        var oResult = volunteerService.viewClientPublicData(username);
        Assertions.assertEquals(Optional.of(new ClientPublicDTO(userPublicDTO)), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(individualRepository, Mockito.never()).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPublicDTO(organization);
    }

    @Test
    void viewClientPublicData_IndividualFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.of(individual));
        Mockito.when(clientMapper.toClientPublicDTO(individual)).thenReturn(new ClientPublicDTO(userPublicDTO));
        var oResult = volunteerService.viewClientPublicData(username);
        Assertions.assertEquals(Optional.of(new ClientPublicDTO(userPublicDTO)), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPublicDTO(individual);
    }

    @Test
    void viewClientPublicData_UserNotFound() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = volunteerService.viewClientPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.never()).toClientPublicDTO(Mockito.any(Individual.class));
    }

    @Test
    void editVolunteerData_DataFound() {
        VolunteerPrivateDTO volunteerPrivateDTO = new VolunteerPrivateDTO(userPrivateDTO, null);
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerRepository.save(volunteer)).thenReturn(volunteer);
        Mockito.when(volunteerMapper.toVolunteerPrivateDTO(volunteer)).thenReturn(volunteerPrivateDTO);
        var oResult = volunteerService.editVolunteerData(volunteerPrivateDTO, username);
        Mockito.verify(userService, Mockito.times(1)).editUserData(userPrivateDTO, username);
        Mockito.verify(volunteerRepository, Mockito.times(1)).save(Mockito.any(Volunteer.class));
        Assertions.assertTrue(oResult.isPresent());
    }

    @Test
    void editVolunteerData_Empty() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = volunteerService.editVolunteerData(new VolunteerPrivateDTO((userPrivateDTO), null), username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(volunteerRepository, Mockito.never()).save(Mockito.any(Volunteer.class));
    }

    @Test
    void listPendingActivities() {
        Mockito.when(activityRepository.findAllByStatusClient(Status.PENDING)).thenReturn(List.of(activity));
        Mockito.when(activityDTOMapper.toVolunteerActivityDTO(activity)).thenReturn(activityDTO);
        var result = volunteerService.listAllPendingActivities();
        Assertions.assertEquals(List.of(activityDTO), result);
    }

    @Test
    void listPendingActivities_Empty() {
        Mockito.when(activityRepository.findAllByStatusClient(Status.PENDING)).thenReturn(List.of());
        var result = volunteerService.listAllPendingActivities();
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void searchPendingActivitiesByText() {
        Mockito.when(activityRepository.findAllByStatusClient(Status.PENDING)).thenReturn(List.of(activity));
        Mockito.when(activityTextSearchService.searchActivitiesByText(List.of(activity), "test")).thenReturn(List.of(activity));
        Mockito.when(activityDTOMapper.toVolunteerActivityDTO(activity)).thenReturn(activityDTO);
        var result = volunteerService.searchPendingActivitiesByText("test");
        Assertions.assertEquals(List.of(activityDTO), result);
    }

    @Test
    void searchPendingActivitiesByText_Empty() {
        Mockito.when(activityTextSearchService.searchActivitiesByText(List.of(activity), "test")).thenReturn(List.of());
        var result = volunteerService.searchPendingActivitiesByText("test");
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void listAllActivitiesOfVolunteer() {
        volunteer.getUser().setActivities(List.of(activity));
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.of(volunteer));
        Mockito.when(activityDTOMapper.toVolunteerActivityDTO(activity)).thenReturn(activityDTO);
        var result = volunteerService.listAllActivitiesOfVolunteer("test");
        Assertions.assertEquals(List.of(activityDTO), result);
    }

    @Test
    void listAllActivitiesOfVolunteer_Empty() {
        volunteer.getUser().setActivities(List.of(activity));
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.empty());
        var result = volunteerService.listAllActivitiesOfVolunteer("test");
        Assertions.assertEquals(List.of(), result);
    }

    // registerNewKeyword() Test
    @Test
    void registerNewKeyword() {
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>());
        volunteerService.registerNewKeyword("cook", DAILY, "test");
        var result = volunteer.getRegisteredKeywords();
        Assertions.assertEquals(Map.of("cook", DAILY), result);
    }

    @Test
    void registerNewKeyword_EmailScheduleNONE() {
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>());
        volunteerService.registerNewKeyword("cook", NONE, "test");
        var result = volunteer.getRegisteredKeywords();
        Assertions.assertEquals(Map.of(), result);
    }

    //TODO: sendEmailsForMatchingKeywords() Test
    @Test
    void sendEmailsForMatchingKeywords() {

    }


    // viewKeywordRegistrations() Test
    @Test
    void viewKeywordRegistrations() {
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>(Map.of("cook", DAILY)));
        var expected = volunteer.getRegisteredKeywords();
        var result = volunteerService.viewKeywordRegistrations("test");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void viewKeywordRegistrations_EmptyMap() {
        Mockito.when(volunteerRepository.findOneByUser_username("test")).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>());
        var expected = volunteer.getRegisteredKeywords();
        var result = volunteerService.viewKeywordRegistrations("test");
        Assertions.assertEquals(expected, result);
    }

    @Test
    void deleteKeywordRegistration() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>(Map.of("cook", DAILY, "garden", WEEKLY)));
        volunteerService.deleteKeywordRegistration("cook", username);
        var result = volunteer.getRegisteredKeywords();
        Assertions.assertEquals(Map.of("garden", WEEKLY), result);
    }

    @Test
    void deleteKeywordRegistration_removeNoEntryIfNoMatch() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        volunteer.setRegisteredKeywords(new HashMap<>(Map.of("cook", DAILY, "garden", WEEKLY)));
        volunteerService.deleteKeywordRegistration("pilot", username);
        var result = volunteer.getRegisteredKeywords();
        Assertions.assertEquals(Map.of("garden", WEEKLY, "cook", DAILY), result);
    }

}