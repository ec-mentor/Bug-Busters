package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.data.UserPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.data.UserPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.service.UserDTOMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class VolunteerDTOMapperTest {

    @Autowired
    VolunteerDTOMapper volunteerDTOMapper;

    @MockBean
    UserDTOMapper userDTOMapper;

    @MockBean
    SetToStringMapper setToStringMapper;

    private final String username = "test";
    private final String skills = "skills";
    private final User user = new User("test", "test", "test",
            "test", LocalDate.of(2000, 1, 1), "test",
            "test", "test");
    private final UserPrivateDTO userPrivateDTO = new UserPrivateDTO(username, user.getRole(), user.getFullName(), user.getBirthday(), user.getAddress(), user.getEmail(), user.getDescription());
    private final UserPublicDTO userPublicDTO = new UserPublicDTO(username, "test", 1, "test", 5.0, 0);
    private final Volunteer volunteer = new Volunteer(user);


    @Test
    void toVolunteerPrivateDTO(){
        Mockito.when(userDTOMapper.toUserPrivateDTO(volunteer.getUser())).thenReturn(userPrivateDTO);
        Mockito.when(setToStringMapper.convertToString(volunteer.getSkills())).thenReturn(skills);
        VolunteerPrivateDTO result = volunteerDTOMapper.toVolunteerPrivateDTO(volunteer);
        VolunteerPrivateDTO expected = new VolunteerPrivateDTO(userPrivateDTO, skills);
        Assertions.assertEquals(expected, result);
        Mockito.verify(userDTOMapper).toUserPrivateDTO(volunteer.getUser());
        Mockito.verify(setToStringMapper).convertToString(volunteer.getSkills());
    }

    @Test
    void toVolunteerPublicDTO(){
        Mockito.when(userDTOMapper.toUserPublicDTO(volunteer.getUser())).thenReturn(userPublicDTO);
        Mockito.when(setToStringMapper.convertToString(volunteer.getSkills())).thenReturn(skills);
        VolunteerPublicDTO result = volunteerDTOMapper.toVolunteerPublicDTO(volunteer);
        VolunteerPublicDTO expected = new VolunteerPublicDTO(userPublicDTO, skills);
        Assertions.assertEquals(expected, result);
        Mockito.verify(userDTOMapper).toUserPublicDTO(volunteer.getUser());
        Mockito.verify(setToStringMapper).convertToString(volunteer.getSkills());
    }
}