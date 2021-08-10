package bugbusters.everyonecodes.java.usermanagement.rolemanagement;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RoleFactoryTest {

    @Autowired
    RoleFactory roleFactory;

    @MockBean
    VolunteerRepository volunteerRepository;
    @MockBean
    OrganizationRepository organizationRepository;
    @MockBean
    IndividualRepository individualRepository;

    private final User testUser = new User(null, null, null, "", null, null, null, null, null);

    //ToDo: Run tests

    @Test
    void createRole_Volunteer() {
        testUser.setRole("ROLE_VOLUNTEER");
        roleFactory.createRole(testUser);
        verify(volunteerRepository, times(1)).save(new Volunteer(testUser));
        verify(organizationRepository, never()).save(new Organization(testUser));
        verify(individualRepository, never()).save(new Individual(testUser));
    }

    @Test
    void createRole_Organization() {
        testUser.setRole("ROLE_ORGANIZATION");
        roleFactory.createRole(testUser);
        verify(volunteerRepository, never()).save(new Volunteer(testUser));
        verify(organizationRepository, times(1)).save(new Organization(testUser));
        verify(individualRepository, never()).save(new Individual(testUser));
    }

    @Test
    void createRole_Individual() {
        testUser.setRole("ROLE_INDIVIDUAL");
        roleFactory.createRole(testUser);
        verify(volunteerRepository, never()).save(new Volunteer(testUser));
        verify(organizationRepository, never()).save(new Organization(testUser));
        verify(individualRepository, times(1)).save(new Individual(testUser));
    }

    @Test
    void createRole_invalidInput() {
        testUser.setRole("");
        Assertions.assertThrows(IllegalArgumentException.class, () -> roleFactory.createRole(testUser));
        verify(volunteerRepository, never()).save(new Volunteer(testUser));
        verify(organizationRepository, never()).save(new Organization(testUser));
        verify(individualRepository, never()).save(new Individual(testUser));

    }
}