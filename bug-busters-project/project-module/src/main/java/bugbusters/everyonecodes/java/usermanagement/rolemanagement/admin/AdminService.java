package bugbusters.everyonecodes.java.usermanagement.rolemanagement.admin;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.Volunteer;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final IndividualRepository individualRepository;
    private final AdminDTOMapper mapper;

    public AdminService(VolunteerRepository volunteerRepository,
                        OrganizationRepository organizationRepository,
                        IndividualRepository individualRepository,
                        AdminDTOMapper mapper) {
        this.volunteerRepository = volunteerRepository;
        this.organizationRepository = organizationRepository;
        this.individualRepository = individualRepository;
        this.mapper = mapper;
    }

    public List<AdminDTO> listAllVolunteers() {
        List<Volunteer> volunteers = volunteerRepository.findAll();
        return volunteers.stream()
                .map(e -> mapper.toAdminDTO(e.getUser()))
                .collect(Collectors.toList());
    }

    public List<AdminDTO> listAllOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();
        return organizations.stream()
                .map(e -> mapper.toAdminDTO(e.getUser()))
                .collect(Collectors.toList());
    }

    public List<AdminDTO> listAllIndividuals() {
        List<Individual> individuals = individualRepository.findAll();
        return individuals.stream()
                .map(e -> mapper.toAdminDTO(e.getUser()))
                .collect(Collectors.toList());
    }

    public Object getAccountDataByUsername(String username) {
        Optional<Volunteer> volunteer = volunteerRepository.findOneByUser_username(username);
        Optional<Organization> organization = organizationRepository.findOneByUser_username(username);
        Optional<Individual> individual = individualRepository.findOneByUser_username(username);
        if (volunteer.isPresent()) {
            return volunteer.get();
        }
        if (organization.isPresent()) {
            return organization.get();
        }
        if (individual.isPresent()) {
            return individual.get();
        }
        return null;
    }

}
