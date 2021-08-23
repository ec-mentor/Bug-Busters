package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.activities.ActivityDTO;
import bugbusters.everyonecodes.java.activities.ActivityDTOMapper;
import bugbusters.everyonecodes.java.activities.ActivityRepository;
import bugbusters.everyonecodes.java.activities.Status;
import bugbusters.everyonecodes.java.search.FilterVolunteer;
import bugbusters.everyonecodes.java.search.FilterVolunteerService;
import bugbusters.everyonecodes.java.search.VolunteerTextSearchService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.*;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;
    private final ClientDTOMapper clientMapper;
    private final VolunteerDTOMapper volunteerMapper;
    private final VolunteerTextSearchService volunteerTextSearchService;
    private final ActivityDTOMapper activityDTOMapper;
    private final ActivityRepository activityRepository;
    private final FilterVolunteerService filterVolunteerService;

    public OrganizationService(OrganizationRepository organizationRepository, UserService userService, VolunteerRepository volunteerRepository, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper, VolunteerTextSearchService volunteerTextSearchService, ActivityDTOMapper activityDTOMapper, ActivityRepository activityRepository, FilterVolunteerService filterVolunteerService) {
        this.organizationRepository = organizationRepository;
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
        this.volunteerTextSearchService = volunteerTextSearchService;
        this.activityDTOMapper = activityDTOMapper;
        this.activityRepository = activityRepository;
        this.filterVolunteerService = filterVolunteerService;
    }

    public Optional<ClientPrivateDTO> editOrganizationData(ClientPrivateDTO input, String username) {
        Optional<Organization> oOrganization = getOrganizationByUsername(username);
        if(oOrganization.isEmpty()) return Optional.empty();
        userService.editUserData(input.getUserPrivateDTO(), username);
        Organization organization = getOrganizationByUsername(username).get();
        //edit properties here
        organization = organizationRepository.save(organization);
        return Optional.of(clientMapper.toClientPrivateDTO(organization));
    }

    public Optional<Organization> getOrganizationByUsername(String username) {
        return organizationRepository.findOneByUser_username(username);
    }

    public Optional<ClientPrivateDTO> viewOrganisationPrivateData(String username) {
        return getOrganizationByUsername(username).map(organization -> clientMapper.toClientPrivateDTO(organization));
    }

    public Optional<ClientPublicDTO> viewOrganisationPublicData(String username) {
        return getOrganizationByUsername(username).map(organization -> clientMapper.toClientPublicDTO(organization));
    }

    public Optional<VolunteerPublicDTO> viewVolunteerPublicData(String username) {
        return volunteerRepository.findOneByUser_username(username).map(volunteer -> volunteerMapper.toVolunteerPublicDTO(volunteer));
    }

    public List<VolunteerSearchResultDTO> listAllVolunteers() {
        return volunteerRepository.findAll().stream()
                .map(volunteer -> volunteerMapper.toVolunteerSearchResultDTO(volunteer))
                .collect(Collectors.toList());
    }

    public List<VolunteerSearchResultDTO> searchVolunteersByText(String text) {
        List<Volunteer> filteredList = volunteerTextSearchService.searchVolunteersByText(volunteerRepository.findAll(), text);
        return filteredList.stream()
                .map(volunteer -> volunteerMapper.toVolunteerSearchResultDTO(volunteer))
                .collect(Collectors.toList());
    }

    public List<VolunteerSearchResultDTO> searchVolunteersByTextFiltered(String text, FilterVolunteer filterVolunteer){
        List<Volunteer> filteredList = volunteerTextSearchService.searchVolunteersByText(volunteerRepository.findAll(), text);
        filteredList = filterVolunteerService.filterSearchResults(filteredList, filterVolunteer);
        return filteredList.stream()
                .map(volunteer -> volunteerMapper.toVolunteerSearchResultDTO(volunteer))
                .collect(Collectors.toList());
    }

    public List<ActivityDTO> listAllActivitiesOfOrganization(String username) {
        var result = activityRepository.findAllByCreator(username);
        return result.stream()
                .map(activity -> activityDTOMapper.toClientActivityDTO(activity))
                .collect(Collectors.toList());
    }

    //ToDO: Tests
    public List<ActivityDTO> listAllDraftsOfOrganization(String username) {
        var result = activityRepository.findAllByCreatorAndStatusClient(username, Status.DRAFT);
        return result.stream()
                .map(activity -> activityDTOMapper.toClientActivityDTO(activity))
                .collect(Collectors.toList());
    }

}
