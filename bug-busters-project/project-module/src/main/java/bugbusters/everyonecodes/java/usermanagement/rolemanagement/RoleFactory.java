package bugbusters.everyonecodes.java.usermanagement.rolemanagement;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleFactory {

    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final IndividualRepository individualRepository;

    public RoleFactory(VolunteerRepository volunteerRepository, OrganizationRepository organizationRepository, IndividualRepository individualRepository) {
        this.volunteerRepository = volunteerRepository;
        this.organizationRepository = organizationRepository;
        this.individualRepository = individualRepository;
    }

    public void createRole(User user) {
        switch (user.getRole()) {
            case "ROLE_VOLUNTEER" -> volunteerRepository.save(new Volunteer(user));
            case "ROLE_ORGANIZATION" -> organizationRepository.save(new Organization(user));
            case "ROLE_INDIVIDUAL" -> individualRepository.save(new Individual(user));
            default -> throw new IllegalArgumentException();
        }
    }
}
