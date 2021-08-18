package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.activities.Activity;
import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityDTOMapper;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.search.VolunteerTextSearchService;
import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.*;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class OrganizationServiceTest {

    @Autowired
    OrganizationService organizationService;

    @MockBean
    VolunteerRepository volunteerRepository;

    @MockBean
    OrganizationRepository organizationRepository;

    @MockBean
    UserService userService;

    @MockBean
    ClientDTOMapper clientMapper;

    @MockBean
    VolunteerDTOMapper volunteerMapper;

    @MockBean
    VolunteerTextSearchService volunteerTextSearchService;

    @MockBean
    ActivityDTOMapper activityDTOMapper;

    // for testing
    private final String username = "test";
    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final UserPrivateDTO userPrivateDTO = new UserPrivateDTO(username, user.getRole(), user.getFullName(), user.getBirthday(), user.getAddress(), user.getEmail(), user.getDescription());
    private final UserPublicDTO userPublicDTO = new UserPublicDTO(username, "test", 1, "test", 5.0);
    private final Organization organization = new Organization(user);
    private final Volunteer volunteer = new Volunteer(user);
    private final VolunteerSearchResultDTO volunteerSearchResultDTO = new VolunteerSearchResultDTO("test", null, null);
    private final Activity activity = new Activity("test", "test", "test", Set.of("test"), Set.of("test"), LocalDateTime.now(), LocalDateTime.now(), true, Status.PENDING, Status.PENDING, null, null, null, null);
    private final ActivityDTO activityDTO = new ActivityDTO("test", "test", "test", Status.PENDING, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null, null, null);

    @Test
    void getOrganizationByUsername() {
        organizationService.getOrganizationByUsername(username);
        Mockito.verify(organizationRepository).findOneByUser_username(username);
    }

    @Test
    void viewOrganisationPrivateData_UserFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.of(organization));
        Mockito.when(clientMapper.toClientPrivateDTO(organization)).thenReturn(new ClientPrivateDTO(userPrivateDTO));
        var oResult = organizationService.viewOrganisationPrivateData(username);
        Assertions.assertEquals(Optional.of(new ClientPrivateDTO(userPrivateDTO)), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPrivateDTO(organization);
    }

    @Test
    void viewOrganisationPrivateData_UserNotFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = organizationService.viewOrganisationPrivateData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.never()).toClientPrivateDTO(Mockito.any(Individual.class));
    }

    @Test
    void viewOrganisationPublicData_UserFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.of(organization));
        Mockito.when(clientMapper.toClientPublicDTO(organization)).thenReturn(new ClientPublicDTO(userPublicDTO));
        var oResult = organizationService.viewOrganisationPublicData(username);
        Assertions.assertEquals(Optional.of(new ClientPublicDTO(userPublicDTO)), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPublicDTO(organization);
    }

    @Test
    void viewOrganisationPublicData_UserNotFound() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = organizationService.viewOrganisationPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(organizationRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.never()).toClientPublicDTO(Mockito.any(Individual.class));
    }

    @Test
    void viewVolunteerPublicData_UserFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerPublicDTO(volunteer)).thenReturn(new VolunteerPublicDTO(userPublicDTO, "skills"));
        var oResult = organizationService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.of(new VolunteerPublicDTO(userPublicDTO, "skills")), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.times(1)).toVolunteerPublicDTO(volunteer);
    }

    @Test
    void viewVolunteerPublicData_UserNotFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = organizationService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.never()).toVolunteerPublicDTO(Mockito.any(Volunteer.class));
    }

    @Test
    void editOrganizationData_DataFound() {
        ClientPrivateDTO clientPrivateDTO = new ClientPrivateDTO(userPrivateDTO);
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.of(organization));
        Mockito.when(organizationRepository.save(organization)).thenReturn(organization);
        Mockito.when(clientMapper.toClientPrivateDTO(organization)).thenReturn(clientPrivateDTO);
        var oResult = organizationService.editOrganizationData(clientPrivateDTO, username);
        Mockito.verify(userService, Mockito.times(1)).editUserData(userPrivateDTO, username);
        Mockito.verify(organizationRepository, Mockito.times(1)).save(Mockito.any(Organization.class));
        Assertions.assertTrue(oResult.isPresent());
    }

    @Test
    void editOrganizationData_Empty() {
        Mockito.when(organizationRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = organizationService.editOrganizationData(new ClientPrivateDTO(userPrivateDTO), username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(organizationRepository, Mockito.never()).save(Mockito.any(Organization.class));
    }

    @Test
    void listAllVolunteers() {
        Mockito.when(volunteerRepository.findAll()).thenReturn(List.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerSearchResultDTO(volunteer)).thenReturn(volunteerSearchResultDTO);
        var result = organizationService.listAllVolunteers();
        Assertions.assertEquals(List.of(volunteerSearchResultDTO), result);
    }

    @Test
    void listAllVolunteers_Empty() {
        Mockito.when(volunteerRepository.findAll()).thenReturn(List.of());
        var result = organizationService.listAllVolunteers();
        Assertions.assertEquals(List.of(), result);
    }


    @Test
    void searchVolunteersByText() {
        Mockito.when(volunteerRepository.findAll()).thenReturn(List.of(volunteer));
        Mockito.when(volunteerTextSearchService.searchVolunteersByText(List.of(volunteer), "test")).thenReturn(List.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerSearchResultDTO(volunteer)).thenReturn(volunteerSearchResultDTO);
        var result = organizationService.searchVolunteersByText("test");
        Assertions.assertEquals(List.of(volunteerSearchResultDTO), result);
    }

    @Test
    void searchVolunteersByText_Empty() {
        Mockito.when(volunteerRepository.findAll()).thenReturn(List.of(volunteer));
        Mockito.when(volunteerTextSearchService.searchVolunteersByText(List.of(volunteer), "test")).thenReturn(List.of());
        var result = organizationService.searchVolunteersByText("test");
        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void listAllMyActivities() {
        organization.getUser().setActivities(List.of(activity));
        Mockito.when(organizationRepository.findOneByUser_username("test")).thenReturn(Optional.of(organization));
        Mockito.when(activityDTOMapper.toClientActivityDTO(activity)).thenReturn(activityDTO);
        var result = organizationService.listAllActivitiesOfOrganization("test");
        Assertions.assertEquals(List.of(activityDTO), result);
    }

    @Test
    void listAllMyActivities_Empty() {
        organization.getUser().setActivities(List.of(activity));
        Mockito.when(organizationRepository.findOneByUser_username("test")).thenReturn(Optional.empty());
        var result = organizationService.listAllActivitiesOfOrganization("test");
        Assertions.assertEquals(List.of(), result);
    }
}