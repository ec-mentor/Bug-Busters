package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerService {
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final IndividualRepository individualRepository;

    /// TODO VolunteerDTOMAPPER!!!!!!
    /// method:: tovolunteerpublicdto, tovolunteerprivatedto

    public VolunteerService(UserService userService, VolunteerRepository volunteerRepository, OrganizationRepository organizationRepository, IndividualRepository individualRepository) {
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.organizationRepository = organizationRepository;
        this.individualRepository = individualRepository;
    }

    Optional<VolunteerPrivateDTO> viewVolunteerPrivateData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }

    Optional<VolunteerPrivateDTO> editProfile(VolunteerPrivateDTO edits, String username) {
        var oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var oUser = userService.editUserData(edits.getUser(), username);

        oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var volunteer = oVolunteer.get();
        volunteer.setSkills(volunteerMapper.toSet(edits.getSkills()));
        volunteer = volunteerRepository.save(volunteer);
        return Optional.of(volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }


    Optional<VolunteerPublicDTO> viewOwnProfile(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPublicDTO(volunteer));
    }

    Optional<ClientPublicDTO> viewClientProfile(String name) {
        return getClientByUsername(name).map(client -> clientMapper.toClientPublicDTO(client));
    }


    public Optional<Volunteer> getVolunteerByUsername(String username) {
        return volunteerRepository.findOneByUser_username(username);
    }

    public Optional<Client> getClientByUsername(String username) {
        Optional<Organization> oOrganization = organizationRepository.findOneByUser_username(username);
        if (oOrganization.isPresent()) {
            return oOrganization.map(Client.class::cast);
        }
        Optional<Individual> oIndividual = individualRepository.findOneByUser_username(username);
        return oIndividual.map(Client.class::cast);
    }
}
