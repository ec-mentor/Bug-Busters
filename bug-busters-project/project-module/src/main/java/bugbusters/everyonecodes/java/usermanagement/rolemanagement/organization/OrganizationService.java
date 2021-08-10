package bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization;

import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.VolunteerRepository;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final VolunteerRepository volunteerRepository;
    private final ClientDTOMapper clientMapper;
    private final VolunteerDTOMapper volunteerMapper;

    public OrganizationService(OrganizationRepository organizationRepository, UserService userService, VolunteerRepository volunteerRepository, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper) {
        this.organizationRepository = organizationRepository;
        this.userService = userService;
        this.volunteerRepository = volunteerRepository;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
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

    public Optional<ClientPrivateDTO> viewOrganisationPrivateData(String username) {
        return getOrganizationByUsername(username).map(organization -> clientMapper.toClientPrivateDTO(organization));
    }

    public Optional<ClientPublicDTO> viewOrganisationPublicData(String username) {
        return getOrganizationByUsername(username).map(organization -> clientMapper.toClientPublicDTO(organization));
    }

    public Optional<VolunteerPublicDTO> viewVolunteerPublicData(String username) {
        return volunteerRepository.findOneByUser_username(username).map(volunteer -> volunteerMapper.toVolunteerPublicDTO(volunteer));
    }

    public Optional<Organization> getOrganizationByUsername(String username) {
        return organizationRepository.findOneByUser_username(username);
    }


}
