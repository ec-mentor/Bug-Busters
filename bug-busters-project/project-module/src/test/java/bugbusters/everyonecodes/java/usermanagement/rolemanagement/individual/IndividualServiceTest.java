package bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class IndividualServiceTest {

    @Autowired
    IndividualService individualService;

    @MockBean
    VolunteerRepository volunteerRepository;

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
    private final Volunteer volunteer = new Volunteer(user);


    @Test
    void getIndividualByUsername() {
        individualService.getIndividualByUsername(username);
        Mockito.verify(individualRepository).findOneByUser_username(username);
    }

    @Test
    void viewIndividualPrivateData_UserFound() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.of(individual));
        Mockito.when(clientMapper.toClientPrivateDTO(individual)).thenReturn(new ClientPrivateDTO(userPrivateDTO));
        var oResult = individualService.viewIndividualPrivateData(username);
        Assertions.assertEquals(Optional.of(new ClientPrivateDTO(userPrivateDTO)), oResult);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPrivateDTO(individual);
    }

    @Test
    void viewIndividualPrivateData_UserNotFound() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = individualService.viewIndividualPrivateData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.never()).toClientPrivateDTO(Mockito.any(Individual.class));
    }

    @Test
    void viewIndividualPublicData_UserFound() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.of(individual));
        Mockito.when(clientMapper.toClientPublicDTO(individual)).thenReturn(new ClientPublicDTO(userPublicDTO));
        var oResult = individualService.viewIndividualPublicData(username);
        Assertions.assertEquals(Optional.of(new ClientPublicDTO(userPublicDTO)), oResult);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.times(1)).toClientPublicDTO(individual);
    }

    @Test
    void viewIndividualPublicData_UserNotFound() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = individualService.viewIndividualPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(individualRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(clientMapper, Mockito.never()).toClientPublicDTO(Mockito.any(Individual.class));
    }

    @Test
    void viewVolunteerPublicData_UserFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.of(volunteer));
        Mockito.when(volunteerMapper.toVolunteerPublicDTO(volunteer)).thenReturn(new VolunteerPublicDTO(userPublicDTO, "skills"));
        var oResult = individualService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.of(new VolunteerPublicDTO(userPublicDTO, "skills")), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.times(1)).toVolunteerPublicDTO(volunteer);
    }

    @Test
    void viewVolunteerPublicData_UserNotFound() {
        Mockito.when(volunteerRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = individualService.viewVolunteerPublicData(username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(volunteerRepository, Mockito.times(1)).findOneByUser_username(username);
        Mockito.verify(volunteerMapper, Mockito.never()).toVolunteerPublicDTO(Mockito.any(Volunteer.class));
    }

    @Test
    void editIndividualData_DataFound() {
        ClientPrivateDTO clientPrivateDTO = new ClientPrivateDTO(userPrivateDTO);
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.of(individual));
        Mockito.when(individualRepository.save(individual)).thenReturn(individual);
        Mockito.when(clientMapper.toClientPrivateDTO(individual)).thenReturn(clientPrivateDTO);
        var oResult = individualService.editIndividualData(clientPrivateDTO, username);
        Mockito.verify(userService, Mockito.times(1)).editUserData(userPrivateDTO, username);
        Mockito.verify(individualRepository, Mockito.times(1)).save(Mockito.any(Individual.class));
        Assertions.assertTrue(oResult.isPresent());
    }

    @Test
    void editIndividualData_Empty() {
        Mockito.when(individualRepository.findOneByUser_username(username)).thenReturn(Optional.empty());
        var oResult = individualService.editIndividualData(new ClientPrivateDTO(userPrivateDTO), username);
        Assertions.assertEquals(Optional.empty(), oResult);
        Mockito.verify(individualRepository, Mockito.never()).save(Mockito.any(Individual.class));
    }

}