package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

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
    UserService userService;

    @MockBean
    ClientDTOMapper clientMapper;

    @MockBean
    VolunteerDTOMapper volunteerMapper;

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

    @Test
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

}