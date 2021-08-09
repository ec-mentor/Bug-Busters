package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.usermanagement.data.User;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerService {
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;

    /// TODO VolunteerDTOMAPPER!!!!!!
    /// method:: tovolunteerpublicdto, tovolunteerprivatedto
    /// ClientPublicDTO, ClientRepository

    public VolunteerService(UserService userService, VolunteerRepository volunteerRepository) {
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
    }

    Optional<VolunteerPrivateDTO> viewVolunteerPrivateData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> mapper.toVolunteerPrivateDTO(volunteer));
    }

    Optional<VolunteerPrivateDTO> editProfile(VolunteerPrivateDTO edits, String username) {
        var oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var oUser = userService.editUserData(edits.getUser(), username);

        oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var volunteer = oVolunteer.get();
        volunteer.setSkills(mapper.toSet(edits.getSkills()));

        return Optional.of(mapper.toVolunteerPrivateDTO(volunteer));
    }


    Optional<VolunteerPublicDTO> viewOwnProfile(String username) {
        return getVolunteerByUsername(username).map(volunteer -> mapper.toVolunteerPublicDTO(volunteer));
    }

    Optional<ClientPublicDTO> viewClientProfile(String name) {
        return getClientByUsername(name).map(client -> mapper.toClientPublicDTO(client));
    }


    public Optional<Volunteer> getVolunteerByUsername(String username) {
        return volunteerRepository.findOneByUser_username(username);
    }

    public Optional<Client> getClientByUsername(String username) {
        var oClient = organizationRepository.findOneByUser_username(username);
        if (oClient.isPresent()) return oClient;
        return individualRepository.findOneByUser_username(username);
    }

}
