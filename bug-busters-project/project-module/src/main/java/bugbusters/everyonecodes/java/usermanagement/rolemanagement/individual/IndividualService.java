package bugbusters.everyonecodes.java.usermanagement.rolemanagement.individual;

import bugbusters.everyonecodes.java.search.ClientTextSearchService;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientDTOMapper;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPrivateDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.organization.ClientPublicDTO;
import bugbusters.everyonecodes.java.usermanagement.rolemanagement.volunteer.*;
import bugbusters.everyonecodes.java.usermanagement.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IndividualService {
    private final VolunteerRepository volunteerRepository;
    private final IndividualRepository individualRepository;
    private final UserService userService;
    private final ClientDTOMapper clientMapper;
    private final VolunteerDTOMapper volunteerMapper;
    private final ClientTextSearchService searchService;


    public IndividualService(VolunteerRepository volunteerRepository, IndividualRepository individualRepository, UserService userService, ClientDTOMapper clientMapper, VolunteerDTOMapper volunteerMapper, ClientTextSearchService searchService) {
        this.volunteerRepository = volunteerRepository;
        this.individualRepository = individualRepository;
        this.userService = userService;
        this.clientMapper = clientMapper;
        this.volunteerMapper = volunteerMapper;
        this.searchService = searchService;
    }

    public Optional<ClientPrivateDTO> editIndividualData(ClientPrivateDTO input, String username) {
        Optional<Individual> optionalIndividual = getIndividualByUsername(username);
        if(optionalIndividual.isEmpty()) return Optional.empty();
        userService.editUserData(input.getUserPrivateDTO(), username);
        Individual individual = getIndividualByUsername(username).get();
        //edit properties here
        individual = individualRepository.save(individual);
        return Optional.of(clientMapper.toClientPrivateDTO(individual));
    }

    public Optional<Individual> getIndividualByUsername(String username) {
        return individualRepository.findOneByUser_username(username);
    }

    public Optional<ClientPrivateDTO> viewIndividualPrivateData(String username) {
        return getIndividualByUsername(username).map(organization -> clientMapper.toClientPrivateDTO(organization));
    }

    public Optional<ClientPublicDTO> viewIndividualPublicData(String username) {
        return getIndividualByUsername(username).map(organization -> clientMapper.toClientPublicDTO(organization));
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
