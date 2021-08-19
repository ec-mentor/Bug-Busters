package bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer;

import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.activities.*;
import bugbusters.everyonecodes.java.search.ActivityTextSearchService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.Client;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.Individual;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual.IndividualRepository;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.Organization;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.OrganizationRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VolunteerService {
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;
    private final OrganizationRepository organizationRepository;
    private final IndividualRepository individualRepository;
    private final ClientDTOMapper clientMapper;
    private final VolunteerDTOMapper volunteerMapper;
    private final SetToStringMapper setToStringMapper;
    private final ActivityDTOMapper activityDTOMapper;
    private final ActivityTextSearchService activityTextSearchService;
    private final ActivityRepository activityRepository;

    public VolunteerService(UserService userService, VolunteerRepository volunteerRepository, OrganizationRepository organizationRepository, IndividualRepository individualRepository, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper, SetToStringMapper setToStringMapper, ActivityDTOMapper activityDTOMapper, ActivityTextSearchService activityTextSearchService, ActivityRepository activityRepository) {
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.organizationRepository = organizationRepository;
        this.individualRepository = individualRepository;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
        this.setToStringMapper = setToStringMapper;
        this.activityDTOMapper = activityDTOMapper;
        this.activityTextSearchService = activityTextSearchService;
        this.activityRepository = activityRepository;
    }

    public Optional<VolunteerPrivateDTO> editVolunteerData(VolunteerPrivateDTO edits, String username) {
        var oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var oUser = userService.editUserData(edits.getUser(), username);

        oVolunteer = getVolunteerByUsername(username);
        if (oVolunteer.isEmpty()) return Optional.empty();

        var volunteer = oVolunteer.get();
        volunteer.setSkills(setToStringMapper.convertToSet(edits.getSkills()));
        volunteer = volunteerRepository.save(volunteer);
        return Optional.of(volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }

    public Optional<Volunteer> getVolunteerByUsername(String username) {
        return volunteerRepository.findOneByUser_username(username);
    }

    public Optional<VolunteerPrivateDTO> viewVolunteerPrivateData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPrivateDTO(volunteer));
    }

    public Optional<VolunteerPublicDTO> viewVolunteerPublicData(String username) {
        return getVolunteerByUsername(username).map(volunteer -> volunteerMapper.toVolunteerPublicDTO(volunteer));
    }

    private Optional<Client> getClientByUsername(String username) {
        Optional<Organization> oOrganization = organizationRepository.findOneByUser_username(username);
        if (oOrganization.isPresent()) {
            return oOrganization.map(Client.class::cast);
        }
        Optional<Individual> oIndividual = individualRepository.findOneByUser_username(username);
        return oIndividual.map(Client.class::cast);
    }

    public Optional<ClientPublicDTO> viewClientPublicData(String name) {
        return getClientByUsername(name).map(client -> clientMapper.toClientPublicDTO(client));
    }

    public List<ActivityDTO> listAllPendingActivities() {
        return activityRepository.findAllByStatusClient(Status.PENDING).stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> searchPendingActivitiesByText(String text) {
        List<Activity> filteredList = activityTextSearchService.searchActivitiesByText(activityRepository.findAllByStatusClient(Status.PENDING), text);
        return filteredList.stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> listAllActivitiesOfVolunteer(String username) {
        var oResult = volunteerRepository.findOneByUser_username(username);
        if(oResult.isEmpty()) return List.of();
        return oResult.get().getUser().getActivities().stream()
                .map(activityDTOMapper::toVolunteerActivityDTO)
                .collect(Collectors.toList());
    }
}
