package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

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
    private final VolunteerTextSearchService searchService;

    public OrganizationService(OrganizationRepository organizationRepository, UserService userService, VolunteerRepository volunteerRepository, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper, VolunteerTextSearchService searchService) {
        this.organizationRepository = organizationRepository;
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
        this.searchService = searchService;
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
        List<Volunteer> filteredList = searchService.searchVolunteersByText(volunteerRepository.findAll(), text);
        return filteredList.stream()
                .map(volunteer -> volunteerMapper.toVolunteerSearchResultDTO(volunteer))
                .collect(Collectors.toList());
    }

}
